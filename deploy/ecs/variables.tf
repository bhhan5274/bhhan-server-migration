variable "aws_region" {
  description = "Region in which AWS Resources to be created"
  default     = "ap-northeast-2"
}

variable "cluster_id" {
  description = "cluster id"
  type        = string
}

variable "cluster_name" {
  default = "cluster name"
  type    = string
}

variable "task_name" {
  description = "task name"
  type        = string
}

variable "environment" {
  description = "Environment Variable used as a prefix"
  default     = "dev"
}

variable "business_division" {
  description = "Business Division in the large organization this Infrastructure belongs"
  default     = "bhhan"
}

variable "subnets" {
  description = "List of subnet IDs"
  type        = list(string)
}

locals {
  owners      = var.business_division
  environment = var.environment
  name        = "${var.business_division}-${var.environment}"
  common_tags = {
    owners      = local.owners
    environment = local.environment
  }
}

variable "container_port" {
  description = "Port of container"
  type        = number
}

variable "container_cpu" {
  description = "The number of cpu units used by the task"
}

variable "container_memory" {
  description = "The amount (in MiB) of memory used by the task"
}

variable "container_image" {
  description = "Docker image to be launched"
}

variable "aws_alb_target_group_arn" {
  description = "ARN of the alb target group"
}

variable "container_environment" {
  description = "The container environment variables"
  type        = list(object({ name = string, value = string }))
}

variable "service_desired_count" {
  description = "Number of services running in parallel"
  type        = number
}

variable "ecs_service_security_groups" {
  description = "list of security groups"
  type        = list(string)
}
