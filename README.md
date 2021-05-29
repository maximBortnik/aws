ec2-rds-s3:
  Simple spring boot app to upload/download files to S3 and to store a file metadata into RDS.
  App send upload event into sqs queue. App listen to queue and send notification into sns topic to send email
  The app also contains cf-template to create infrostructure in aws
sqs-sns-lambda:
  Simple sping boot app to receive SQSEvent and sent it to SNS using lambda
  The app also contains cf-template to create infrostructure in aws
