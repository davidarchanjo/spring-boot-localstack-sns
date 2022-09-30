![banner](./assets/banner.jpg)

# INTRODUCTION
When building applications for the cloud, we need to be able to make test through and after the development phase in an environment minimally similar or ideally the same as the production environment where the application will run. We who develop for the AWS not always have an AWS account available in which we can simulate the environment, resources and/or services as we have in corporate projects.

With this problem in mind, [LocalStack](https://github.com/localstack/localstack) was conceived to be a fully cloud service emulator for testing and simulation for the major AWS services, e.g. DynamoDB, SNS, SQS, Lambda, we usually use to compose cloud-native applications right from the comfort of our local machine. LocalStack offers almost the same functionality, with some limitations, and APIs as the AWS cloud environment does.


# OBJECTIVES
This project intends to demonstrate the following three points:
- create a SNS topic on LocalStack (via command line)
- publish an event to a SNS topic (via command line)
- subscribe a REST endpoint to consume events from a SNS topic (via command line)


# PREREQUISITES
You should have the following technologies installed on your system:
- [docker-compose](https://docs.docker.com/compose/install/)
- [localstack](https://github.com/localstack/localstack)
- [awscli](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
- [awslocal](https://github.com/localstack/awscli-local)
- [java 14+](https://www.liquidweb.com/kb/how-to-install-java-windows-ubuntu-macos/)
- [gradle](https://gradle.org/install/)

**P.S.** In addition to these tools, you should have a basic understanding on [how to manipulate the command line interface (CLI)](https://www.linuxjournal.com/content/linux-command-line-interface-introduction-guide).


# TESTING
Open three terminal from the folder you downloaded or cloned the repository and go as follows:

## 1. START THE APPLICATION
From the first terminal, run:
```bash
./mvnw spring-boot:run
```

## 2. SNIP UP LOCALSTACK
From the second terminal, run:
```bash
docker-compose -f ./devops/docker-compose.yml up
```
You should get something like on the terminal:
![localstack](./assets/localstack.jpg)

## 3. SET THE ENDPOINT ADDRESS
From the third terminal, run:
```bash
ENDPOINT=http://host.docker.internal:8080
```
**P.S.** In order to be able to connect to services in the host machine from inside a container we should use `host.docker.internal` instead of `localhost` in the endpoint's address.

## 4. CREATE TOPIC
```bash
TOPIC_ARN=$(awslocal sns create-topic --name test-topic --output json | jq -r '.TopicArn')
```

## 5. SUSBSCRIBE ENDPOINT
```bash
awslocal sns subscribe --topic-arn $TOPIC_ARN --notification-endpoint $ENDPOINT --protocol http --attributes RawMessageDelivery=true
```

## 6. SEND MESSAGE
```bash
awslocal sns publish --topic-arn $TOPIC_ARN --message '{"firstName": "David", "lastName": "Archanjo"}'
```
From the first terminal in which the application is running, you should get something like the following:
![event](./assets/event.jpg)

# FINAL NOTES
LocalStack is a tool that should definitely be part of our cloud development stack because it is one of the most viable way to explore AWS services and their features in a controlled, customizable and, most importantly, free of charge environment.

Notice that in this project I made use of only one of the many AWS services provided by LocalStack. To check the complete and up-to-date list of all AWS emulated services and their availability, visit this [link](https://docs.localstack.cloud/aws/feature-coverage/).