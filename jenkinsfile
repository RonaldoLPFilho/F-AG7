pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo "Clonando código..."
                checkout scm
            }
        }

        stage('Testes') {
            steps {
                echo "Rodando testes..."
                dir('auth-service') {
                    sh './mvnw clean test'
                }
            }
        }

        stage('Build Docker') {
            steps {
                echo "Buildando imagens Docker..."
                sh 'docker compose build'
            }
        }

        stage('Deploy') {
            steps {
                echo "Subindo containers..."
                sh 'docker compose up -d'
            }
        }
    }

    post {
        success {
            echo "✅ Deploy simulado com sucesso!"
        }
        failure {
            echo "❌ Algo deu errado no pipeline."
        }
    }
}
