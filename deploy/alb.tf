module "alb" {
  source  = "terraform-aws-modules/alb/aws"
  version = "6.0.0"

  name = "${local.name}-alb"
  load_balancer_type = "application"
  vpc_id = module.vpc.vpc_id
  subnets = module.vpc.public_subnets
  security_groups = [module.loadbalancer_sg.security_group_id]

  http_tcp_listeners = [
    {
      port               = 80
      protocol           = "HTTP"
      action_type = "redirect"
      redirect = {
        port        = "443"
        protocol    = "HTTPS"
        status_code = "HTTP_301"
      }
    }
  ]

  target_groups = [
    {
      name_prefix          = "back-"
      backend_protocol     = "HTTP"
      backend_port         = var.backend_container_port
      target_type          = "ip"
      deregistration_delay = 10
      health_check = {
        enabled             = true
        interval            = 30
        path                = var.backend_health_check_path
        port                = "traffic-port"
        healthy_threshold   = 3
        unhealthy_threshold = 3
        timeout             = 6
        protocol            = "HTTP"
        matcher             = "200-399"
      }
      protocol_version = "HTTP1"
    },
    {
      name_prefix          = "front-"
      backend_protocol     = "HTTP"
      backend_port         = var.frontend_container_port
      target_type          = "ip"
      deregistration_delay = 10
      health_check = {
        enabled             = true
        interval            = 30
        path                = var.frontend_health_check_path
        port                = "traffic-port"
        healthy_threshold   = 3
        unhealthy_threshold = 3
        timeout             = 6
        protocol            = "HTTP"
        matcher             = "200-399"
      }
      protocol_version = "HTTP1"
    },
  ]

  https_listeners = [
    {
      port               = 443
      protocol           = "HTTPS"
      certificate_arn    = var.certification_arn
      action_type = "fixed-response"

      fixed_response = {
        content_type = "text/plain"
        message_body = "Page Not Found"
        status_code  = "400"
      }
    },
  ]

  https_listener_rules = [
    {
      https_listener_index = 0
      actions = [
        {
          type               = "forward"
          target_group_index = 0
        }
      ]
      conditions = [{
        host_headers = [var.backend_dns_name]
      }]
    },
    {
      https_listener_index = 0
      actions = [
        {
          type               = "forward"
          target_group_index = 1
        }
      ]
      conditions = [{
        host_headers = [var.frontend_dns_name]
      }]
    }
  ]
  tags = local.common_tags
}

output "alb_target_group_arns" {
  description = "Alb Target Group Arns"
  value = module.alb.target_group_arns
}
