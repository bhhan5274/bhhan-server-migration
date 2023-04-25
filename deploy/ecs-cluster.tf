resource "aws_ecs_cluster" "main" {
  name = "${local.name}-cluster"
  tags = {
    Name        = "${local.name}-cluster"
    Environment = var.environment
  }
}
