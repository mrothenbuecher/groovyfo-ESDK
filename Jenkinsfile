// TIP: You can setup a Jenkins Instance in your AWS Account by following these instructions using the available CloudFormation Templates:
// Jenkins 2.0: highly available master: http://templates.cloudonaut.io/en/stable/jenkins/
// WARNING:
// Set the parameter MasterInstanceType to at least t2.medium
// Set the parameter MasterVolumeSize to at least 50 GB
// TODO: Docker needs to be installed on your node
// TODO: Restrict the number of available executors to 1
node {
	try {
		stage('Setup') {
			checkout scm
			prepareEnv()
			currentBuild.description = "Setup"
			initGradleProperties()
			startDockerContainers()
		}
		stage('Build') {
			currentBuild.description = "Build"
			sh './gradlew checkPreconditions'
			sh './gradlew fullInstall'
		}
		stage('Test') {
			currentBuild.description = "Test"
			sh './gradlew verify'
			sh './gradlew createAppJar'
		}
	} catch (any) {
		currentBuild.description = currentBuild.description + " failed"
		any.printStackTrace()
		currentBuild.result = 'FAILURE'
		throw any
	} finally {
		stopDockerContainers()

		junit allowEmptyResults: true, testResults: 'build/test-results/**/*.xml'
		archiveArtifacts allowEmptyArchive: true, artifacts: 'build/reports/**'
		archiveArtifacts allowEmptyArchive: true, artifacts: 'build/libs/*.jar'
	}
}

def prepareEnv() {
	// TODO: Configure a jdk version 8 as tool on your Jenkins and name it `jdk-8`
	def jdkHome = tool name: 'jdk-8', type: 'jdk'
	env.PATH = "${jdkHome}/bin:${env.PATH}"
	env.JAVA_HOME = "${jdkHome}"
	env.GRADLE_USER_HOME = "${WORKSPACE}/gradle-home"
	sh """
		mkdir -p ${env.GRADLE_USER_HOME}
		echo 'org.gradle.java.home=${env.JAVA_HOME}' > ${env.GRADLE_USER_HOME}/gradle.properties
	"""
	jdkHome = null
	sh '''
		if [ ! -e $HOME/docker-compose ]; then
			curl -L https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m) -o $HOME/docker-compose
			chmod +x $HOME/docker-compose
		fi
	'''
	sh 'chmod a+x gradlew'
}

def initGradleProperties() {
	sh 'chmod a+x initGradleProperties.sh'
	sh './initGradleProperties.sh'
}

def startDockerContainers() {
	// TODO: Add your credentials for sdp.registry.abas.sh to Jenkins and specify the credentials ID below
	withCredentials([usernamePassword(credentialsId: '<your-credentials-id>',
			passwordVariable: 'PASSWORD', usernameVariable: 'USER')]) {
		sh 'docker login sdp.registry.abas.sh -u $USER -p $PASSWORD'
	}
	sh '$HOME/docker-compose up -d'
	sleep 30
}

def stopDockerContainers() {
	sh '$HOME/docker-compose down || true'
}
