module "vpc" {
  source = "./vpc"
  vpc_name = "my-vpc"
  vpc_cidr_block = "10.0.0.0/16"
  vpc_availability_zones = ["ap-northeast-2a", "ap-northeast-2b"]
  vpc_public_subnets = ["10.0.1.0/24", "10.0.2.0/24"]
  vpc_private_subnets = ["10.0.3.0/24", "10.0.4.0/24"]
  vpc_database_subnets= ["10.0.5.0/24", "10.0.6.0/24"]
  vpc_create_database_subnet_group = true
  vpc_create_database_subnet_route_table = false
  vpc_enable_nat_gateway = false
  vpc_single_nat_gateway = false

  aws_region = var.aws_region
  business_division = var.business_division
  environment = var.environment
}
