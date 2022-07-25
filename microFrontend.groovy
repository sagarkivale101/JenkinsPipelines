import groovy.json.*
import jenkins.model.*


podTemplate(containers:
    [containerTemplate(name: 'node', image: 'node:16', ttyEnabled: true, command: 'cat'),
   
    containerTemplate(name: 'docker', image: 'docker:latest', ttyEnabled: true, command: 'cat')],
   
    volumes: [hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')])  
   
    {
     
      node(POD_LABEL) {
       
         container ("docker") {  
       
            stage('Checkout') {
                sh 'rm -rf *'
               //1. pull microservices code from git
                git credentialsId: 'sagar_bitbucket', url: 'https://bitbucket.org', branch: 'master'
            }
           
            stage('build') {
                sh 'pwd'
                // 1. create Dockerfile to build project
                    sh "echo 'FROM docker' > Dockerfile "
                    sh "echo 'WORKDIR /home/app/gessa' >> Dockerfile "
                    sh "echo 'COPY . .' >> Dockerfile "
                    sh "echo 'RUN apk get update' >> Dockerfile "
                    sh "echo 'RUN apk add --update nodejs npm' >> Dockerfile "
                    sh "echo 'RUN npm i nx -g' >> Dockerfile " 
                    sh "echo 'RUN npm install' >> Dockerfile " 
                    sh "echo 'RUN nx build container' >> Dockerfile " 
                    
                    sh 'cat Dockerfile'
                    sh 'ls'

                    // 2. Build docker image 
                    sh 'docker build -t gessa12 . --network host'
                //    sh 'docker images'
                

                //--
                //3. Copy necessary files from dockerimage built in step 2 to necessary folder
                    sh 'docker create -ti --name dummy gessa12 bash'
                    sh 'docker cp dummy:/home/app/gessa/dist/apps /home/jenkins/agent/workspace/front/server'

                    //-- 
                    sh 'ls server'
                    sh 'ls server/apps'
                    
                    dir('server'){
                        sh 'docker build -t dessacontainer .'
                    }
                    
                    
               
                
                //------------------
            //   sh 'ls -a'
            //     //   sh 'apt-get install sudo'
            // //     sh 'apt-get update'
 	 	        // // sh 'apt-get install sudo'
 	 	        // sh 'chmod -x /home/jenkins/agent/workspace/front/'
 	 	        // sh 'node -v'
 	 	        // sh 'npm -v'
            // //     sh 'rm package-lock.json'
            // //     sh 'ls -a'
            // //     sh 'npm install -g npm@6'
            // //     sh 'npm cache clean --force'
            // //     // sh 'npm install --save'
            // //     sh 'sudo npm install'
            // //   // sh 'npm install'
            // //     sh 'npm i nx -g'
            // //     sh 'nx build container'
            //   // sh 'rm -rf package.lock.json'
            //   // sh 'apk add --update nodejs npm'
                
            //   // sh 'apk add sudo'
            //   sh 'apk add sudo'
            //   sh 'sudo su'
            //   sh 'apk add --no-cache python3 py3-pip'
            //   // sh 'npm install cypress --save-dev'
             
            //     sh 'export CYPRESS_CACHE_FOLDER=/app/.cache'
            //   //  sh 'npm install'
            //   sh 'npm set ca null'
              
            // sh 'npm config set https-proxy null'
            // sh 'npm config set proxy null'
            // sh 'npm config set strict-ssl false'
            //  sh 'npm config set user 0'
            // sh 'sudo npm install -g cypress'
            
            //     sh 'sudo npm install --unsafe-perm --allow-root cypress --force'
                
            //     //----------
                
            }
           
            
           
           
           
            // stage('build push'){
               
            //     script{
                 
            //         container("docker") {
                       
            //             sh 'docker build .'
                        
            //         }
            //     }
            // }
         
           
        
        }
    }
}