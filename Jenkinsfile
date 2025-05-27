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
            }
        }

        stage('Deploy') {
            steps {
                echo "Subindo containers..."
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
