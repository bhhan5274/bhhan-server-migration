environment = "prod"
business_division = "development"
aws_region = "ap-northeast-2"
backend_container_cpu = 512
backend_container_memory = 1024
frontend_container_cpu = 512
frontend_container_memory = 1024
backend_container_image = "bhhan87/server:v1.0.0.RELEASE"
frontend_container_image = "bhhan87/front:v1.0.1.RELEASE"
backend_health_check_path = "/actuator/health"
frontend_health_check_path = "/"
backend_container_port = 8080
frontend_container_port = 3000
backend_service_name = "backend"
frontend_service_name = "frontend"
service_desired_count = 1
container_environment = [
  {
    name = "SPRING_PROFILES_ACTIVE",
    value = "prod"
  },
  {
    name = "CLOUD_AWS_CLOUDFRONT_URL",
    value = "https://d2evcta4zfc3nw.cloudfront.net"
  },
  {
    name = "CLOUD_AWS_S3_BUCKET",
    value = "bhhan-bucket-optimum-mallard"
  }
]
certification_arn = "arn:aws:acm:ap-northeast-2:852093670342:certificate/0ee763d6-3214-4259-a661-f75f8bf2a1e9"
backend_dns_name = "admin.bhhan.com"
frontend_dns_name = "app.bhhan.com"

