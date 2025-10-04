# 🚀 CI/CD DevOps Project Documentation

## 1. Project Overview
This project demonstrates a complete **CI/CD pipeline** for a Java web application.  
It covers the full DevOps lifecycle: **Code → Build → Test → Quality → Docker → AWS ECR → Kubernetes → Notifications**.  

- **App**: Java Servlet (`WAR` build via Maven)  
- **Build Tool**: Maven  
- **Quality Check**: SonarQube (self-hosted in Docker)  
- **Containerization**: Docker  
- **Image Registry**: AWS ECR  
- **Orchestration**: Kubernetes (EKS)  
- **CI/CD**: Jenkins (master + worker nodes)  
- **Notification**: Jenkins Email Extension  

---

## 2. Infrastructure Setup

## Jenkins Master Node (t2.medium minimum)
```bash
sudo apt update
sudo apt install -y openjdk-17-jdk
```
### Jenkins install

```bash
wget -q -O - https://pkg.jenkins.io/debian/jenkins.io.key | sudo apt-key add -
sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt update
sudo apt install -y jenkins
```
### Tools
```bash
sudo apt install -y unzip
```
### AWS CLI
```bash
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```
## Jenkins Worker Node
```bash

sudo apt update
sudo apt install -y openjdk-17-jdk unzip
```
### AWS CLI
```bash
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```
### kubectl install
```bash
curl -Lo kubectl https://dl.k8s.io/release/$(curl -s -L https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl
chmod +x ./kubectl
sudo mv ./kubectl /usr/local/bin/kubectl
```
### Configure AWS
```bash
aws configure
```
## 3. Jenkins Configuration
### Required Plugins
Pipeline: Stage View

Pipeline: AWS Steps

AWS Credentials

SonarQube Scanner

Email Extension Template

### Add Worker Node
Configure via Manage Jenkins → Nodes

Provide SSH credentials to connect worker

### SonarQube Setup
```bash
docker run -d --name sonar -p 9000:9000 sonarqube
```
Access: http://<jenkins-ip>:9000

Create a new project → Generate token

Add token in Jenkins → Manage Credentials (ID: sonar-id)

Add SonarQube server in Jenkins → Configure System

### AWS Credentials
Add AWS Access Key + Secret Key in Jenkins credentials (ID: aws-creds)

### Email Setup
SMTP: smtp.gmail.com

Port: 587 (TLS)

Add Gmail App Password in Jenkins credentials

Configure in Extended E-mail Notification

## 4. CI/CD Pipeline Flow
CODE-PULL → Jenkins pulls code from GitHub repo

SonarQube Analysis → mvn clean verify sonar:sonar

CODE-BUILD → WAR build using Maven

Docker Build & Push → Image pushed to AWS ECR

DEPLOY-TO-EKS → New image deployed with kubectl set image

Notification → Email sent on SUCCESS/FAILURE

## 5. Kubernetes Deployment
```bash
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-deployment
  namespace: default
  labels:
    app: my-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-container
        image: 578294758767.dkr.ecr.us-east-1.amazonaws.com/my-img:latest
        ports:
        - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: my-service
  namespace: default
spec:
  type: LoadBalancer
  selector:
    app: my-app
  ports:
    - port: 80
      targetPort: 8080
```
## 6. Project Workflow Summary
Developer pushes code → GitHub

Jenkins pipeline triggers → Pulls code

SonarQube scans → Code Quality check

Maven builds WAR → Docker builds image

AWS ECR → Stores image

Kubernetes (EKS) → Deploys image with LoadBalancer Service

Jenkins → Sends email notification

⚡ In short:
Java WAR → Docker → AWS ECR → EKS Deployment → Jenkins CI/CD with SonarQube + Email.
