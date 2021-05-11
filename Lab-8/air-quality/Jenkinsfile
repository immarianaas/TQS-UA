pipeline {
	agent any
	tools {
		jdk 'jdk14'
		maven 'mvn'
	}
	stages {
		stage('test java installation') {
			steps {
				dir('Lab-8/air-quality') {
					sh 'java -version'
				}
			}
		}
		stage('test maven installation') {
			steps {
				dir('Lab-8/air-quality') {
					sh 'mvn -version'
				}
			}
		}
	}
}
