variable "aws_access_key" {
  type = string
}

variable "aws_secret_key" {
  type = string
}

variable "username" {
  type = string
}

variable "password" {
  type = string
}

variable "backend_container_port" {
  type = number
}

variable "frontend_container_port" {
  type = number
}

variable "backend_container_image" {
  type = string
}

variable "frontend_container_image" {
  type = string
}

variable "backend_container_cpu" {
  type = number
}

variable "backend_container_memory" {
  type = number
}

variable "frontend_container_cpu" {
  type = number
}

variable "frontend_container_memory" {
  type = number
}

variable "service_desired_count" {
  type = number
}

variable "backend_health_check_path" {
  type = string
}

variable "frontend_health_check_path" {
  type = string
}

variable "container_environment" {
  type = list(object({ name = string, value = string }))
}

variable "certification_arn" {
  type = string
}

variable "backend_service_name" {
  type = string
}

variable "frontend_service_name" {
  type = string
}

variable "backend_dns_name" {
  type = string
}

variable "frontend_dns_name" {
  type = string
}

variable "db_url" {
  type = string
}

variable "db_username" {
  type = string
}

variable "db_password" {
  type = string
}
