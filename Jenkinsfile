pipeline {
    agent any

    stages {

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t fullstack-backend .'
            }
        }

        stage('Stop Old Container') {
            steps {
                sh '''
                docker stop fullstack-container || true
                docker rm fullstack-container || true
                '''
            }
        }

        stage('Create Env File') {
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
                    cat <<EOF > app.env
DB_URL=$DB_URL
DB_USER=$DB_USER
DB_PASS=$DB_PASS
JWT_SECRET=$JWT_SECRET
MAIL_USER=$MAIL_USER
MAIL_PASS=$MAIL_PASS
AWS_ACCESS=$AWS_ACCESS
AWS_SECRET=$AWS_SECRET
EOF
                    '''
                }
            }
        }

        stage('Run Container') {
            steps {
                sh '''
                docker run -d \
                --name fullstack-container \
                -p 9090:9090 \
                --env-file app.env \
                fullstack-backend
                '''
            }
        }
        
        stage('Start Kafka') {
    		steps {
		        sh '''
		        docker network create kafka-net || true
		
		        docker run -d --name zookeeper \
		        --network kafka-net \
		        -p 2181:2181 \
		        -e ZOOKEEPER_CLIENT_PORT=2181 \
		        -e ZOOKEEPER_TICK_TIME=2000 \
		        confluentinc/cp-zookeeper || true
		
		        docker run -d --name kafka \
		        --network kafka-net \
		        -p 9092:9092 \
		        -e KAFKA_BROKER_ID=1 \
		        -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
		        -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
		        -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
		        confluentinc/cp-kafka || true
		        '''
		    }
		}
    }
}