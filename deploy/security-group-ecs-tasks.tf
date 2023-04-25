module "ecs-backend-tasks-sg" {
  source = "terraform-aws-modules/security-group/aws"
  version = "4.0.0"

  name = "${local.name}-ecs-backend-tasks-sg"
  description = "backend security group"
  vpc_id = module.vpc.vpc_id
  ingress_with_cidr_blocks = [
    {
      from_port   = var.backend_container_port
      to_port     = var.backend_container_port
      protocol    = "tcp"
      description = "backend tasks rule"
      cidr_blocks = module.vpc.vpc_cidr_block
    }
  ]
  egress_rules = ["all-all"]
  tags = local.common_tags
}

module "ecs-frontend-tasks-sg" {
  source = "terraform-aws-modules/security-group/aws"
  version = "4.0.0"

  name = "${local.name}-ecs-frontend-tasks-sg"
  description = "frontend security group"
  vpc_id = module.vpc.vpc_id
  ingress_with_cidr_blocks = [
    {
      from_port   = var.frontend_container_port
      to_port     = var.frontend_container_port
      protocol    = "tcp"
      description = "frontend tasks rule"
      cidr_blocks = module.vpc.vpc_cidr_block
    }
  ]
  egress_rules = ["all-all"]
  tags = local.common_tags
}
