resource "aws_route53_record" "backend_app_dns" {
  name    = var.backend_dns_name
  type    = "A"
  zone_id = data.aws_route53_zone.mydomain.zone_id

  alias {
    evaluate_target_health = true
    name                   = module.alb.lb_dns_name
    zone_id                = module.alb.lb_zone_id
  }
}

resource "aws_route53_record" "frontend_app_dns" {
  name    = var.frontend_dns_name
  type    = "A"
  zone_id = data.aws_route53_zone.mydomain.zone_id

  alias {
    evaluate_target_health = true
    name                   = module.alb.lb_dns_name
    zone_id                = module.alb.lb_zone_id
  }
}
