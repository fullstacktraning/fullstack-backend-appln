pipeline {
    agent any

    environment {
        IMAGE_NAME = "fullstack-backend"
        CONTAINER_NAME = "fullstack-container"
    }

    stages {

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Stop Old Container') {
            steps {
                sh '''
                docker stop $CONTAINER_NAME || true
                docker rm $CONTAINER_NAME || true
                '''
            }
        }

        stage('Run Container') {
            steps {
                withCredentials([
                    string(credentialsId: 'DB_URL', variable: 'DB_URL'),
                    string(credentialsId: 'DB_USER', variable: 'DB_USER'),
                    string(credentialsId: 'DB_PASS', variable: 'DB_PASS'),
                    string(credentialsId: 'JWT_SECRET', variable: 'JWT_SECRET'),
                    string(credentialsId: 'MAIL_USER', variable: 'MAIL_USER'),
                    string(credentialsId: 'MAIL_PASS', variable: 'MAIL_PASS'),
                    string(credentialsId: 'AWS_ACCESS', variable: 'AWS_ACCESS'),
                    string(credentialsId: 'AWS_SECRET', variable: 'AWS_SECRET')
                ]) {
                    sh '''
                    docker run -d \
                    --name $CONTAINER_NAME \
                    -p 9090:9090 \
                    -e DB_URL=$DB_URL \
                    -e DB_USER=$DB_USER \
                    -e DB_PASS=$DB_PASS \
                    -e JWT_SECRET=$JWT_SECRET \
                    -e MAIL_USER=$MAIL_USER \
                    -e MAIL_PASS=$MAIL_PASS \
                    -e AWS_ACCESS=$AWS_ACCESS \
                    -e AWS_SECRET=$AWS_SECRET \
                    $IMAGE_NAME
                    '''
                }
            }
        }
    }
}