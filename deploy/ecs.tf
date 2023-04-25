module "ecs_backend" {
  source                      = "./ecs"
  cluster_id                  = aws_ecs_cluster.main.id
  cluster_name                = aws_ecs_cluster.main.name
  task_name                   = var.backend_service_name
  aws_region                  = var.aws_region
  business_division           = var.business_division
  environment                 = var.environment
  aws_alb_target_group_arn    = module.alb.target_group_arns[0]
  subnets                     = module.vpc.public_subnets
  ecs_service_security_groups = [module.ecs-backend-tasks-sg.security_group_id]
  container_image             = var.backend_container_image
  container_cpu               = var.backend_container_cpu
  container_memory            = var.backend_container_memory
  container_port              = var.backend_container_port
  service_desired_count       = var.service_desired_count
  container_environment       = concat([
    {
      name  = "SPRING_DATASOURCE_URL",
      value = var.db_url
    },
    {
      name  = "SPRING_DATASOURCE_USERNAME",
      value = var.db_username
    },
    {
      name  = "SPRING_DATASOURCE_PASSWORD",
      value = var.db_password
    },
    {
      name  = "CLOUD_AWS_CREDENTIALS_ACCESSKEY",
      value = var.aws_access_key
    },
    {
      name  = "CLOUD_AWS_CREDENTIALS_SECRETKEY",
      value = var.aws_secret_key
    },
    {
      name  = "USER_USERNAME",
      value = var.username
    },
    {
      name  = "USER_PASSWORD",
      value = var.password
    },
    {
      name  = "USER_REDIRECTURL",
      value = "https://${var.backend_dns_name}"
    }
  ], var.container_environment)
}

module "ecs_frontend" {
  source                      = "./ecs"
  cluster_id                  = aws_ecs_cluster.main.id
  cluster_name                = aws_ecs_cluster.main.name
  task_name                   = var.frontend_service_name
  aws_region                  = var.aws_region
  business_division           = var.business_division
  environment                 = var.environment
  aws_alb_target_group_arn    = module.alb.target_group_arns[1]
  subnets                     = module.vpc.public_subnets
  ecs_service_security_groups = [module.ecs-frontend-tasks-sg.security_group_id]
  container_image             = var.frontend_container_image
  container_cpu               = var.frontend_container_cpu
  container_memory            = var.frontend_container_memory
  container_port              = var.frontend_container_port
  service_desired_count       = var.service_desired_count
  container_environment       = [
    {
      name  = "FRONT_END_PROPERTY",
      value = "SAMPLE"
    }
  ]
}
