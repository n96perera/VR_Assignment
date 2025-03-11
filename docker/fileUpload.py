import boto3

# LocalStack S3 Configuration
LOCALSTACK_ENDPOINT = "http://localhost:4566"
BUCKET_NAME = "s3-bucket"
FILE_PATH = "template.json"
OBJECT_NAME = "template.json"

# Create S3 client with LocalStack
s3_client = boto3.client(
    "s3",
    endpoint_url=LOCALSTACK_ENDPOINT,
    aws_access_key_id="test",
    aws_secret_access_key="test",
    region_name="eu-west-1"
)

def upload_file():
    try:
        s3_client.upload_file(FILE_PATH, BUCKET_NAME, OBJECT_NAME)
        print(f"✅ File '{FILE_PATH}' uploaded successfully to bucket '{BUCKET_NAME}'")
    except Exception as e:
        print(f"❌ Failed to upload file: {e}")

if __name__ == "__main__":
    upload_file()
