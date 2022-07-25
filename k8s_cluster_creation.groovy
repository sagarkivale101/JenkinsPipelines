pipeline{
    agent any
    
    stages{
        stage('Clean Workspace'){
            
          steps {
            script{
                cleanWs()
            }
          }
        }
        
        
        stage('Checkout'){
            steps {
                
                
                    git url: 'https://github.com/sagarkivale101/terraformEKS.git', branch : "master"
                    sh 'ls -a'
                    
            }
        }
        
         stage('edit'){
            steps {
                    
                    sh 'ls'
                    sh "sed -i 's/gessa/${ClusterName}/g' vpc6.tf"
                    sh "sed -i 's/us-east-2/${ClusterRegion}/g' vpc6.tf"
                    
                    sh "sed -i 's/workergrpname/${workerGroupName}/g' eks-cluster1.tf"
                    sh "sed -i 's/instanceType/${Ec2InstanceType}/g' eks-cluster1.tf"
                    sh "sed -i 's/descapacity/${desiredCapacity}/g' eks-cluster1.tf"
                    
                    
                    sh 'cat vpc6.tf'
            }
        }

    
        
        stage('AWS Configuration'){
 
            steps{
                  sh 'apt-get update'
                  sh 'apt install sudo'
                  sh 'apt-get install wget'
                  sh "rm -rf awscliv2.zip"
                  sh "rm -rf aws"
                  sh 'curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"'
                  sh "unzip awscliv2.zip"
                  
                  sh "sudo ./aws/install --update"
                  sh "aws --version"
                  sh "aws configure set aws_access_key_id '${aws_access_key_id}' && aws configure set aws_secret_access_key '${aws_secret_access_key}' && aws configure set region '${region}'"
                
            }
        }
        
        
        stage('Terraform Plan ,Init & apply or Destroy'){
    steps {
        
    
           
      
        sh "sudo apt install wget zip python3-pip -y"
        sh "curl -o terraform.zip https://releases.hashicorp.com/terraform/1.2.3/terraform_1.2.3_linux_amd64.zip"
        sh "unzip terraform.zip"
        sh "sudo mv terraform /usr/bin"
        
        
        // sh "rm -rf terraform.zip"
        
        // sh "rm -rf awscliv2.zip"
        // sh "rm -rf aws"
        
       
        
        sh 'terraform version'
        sh 'terraform init -no-color'
        sh 'terraform plan -no-color'
        sh 'terraform ${action} -auto-approve -no-color'
      
       
     
        

    }
    }
    
    


     stage('push'){
            steps {
                    
                    sh 'rm -rf .git'
                    sh 'ls -a'
                    sh 'git init'
                    sh "echo '.terraform' > .gitignore "
                    sh "echo '.gitignore' >> .gitignore "
                    sh "echo 'aws' >> .gitignore "
                    sh "echo 'awscliv2.zip' >> .gitignore "
                    sh "echo 'terraform.zip' >> .gitignore " 
                    sh 'cat .gitignore'
                    sh 'git config --global user.name "${GituserName}"'
                    sh 'git config --global user.email "${GitEmail}"'
                    
                    sh 'git checkout -b ${Gitbranch}' 
                    sh 'git status'
                    sh 'git add .'
                    sh "git commit -m 'test'"
                    sh 'git status'
                    
                    sh 'git remote add origin https://${GituserName}:${gitToken}@github.com/${GituserName}/${gitRepo}.git'
                   
                    //git remote add origin https://[USERNAME]:[NEW TOKEN]@github.com/[USERNAME]/[REPO].git
                    
                    //sh 'git remote add origin https://github.com/sagarkivale101/test.git'
                    sh 'git remote -v'
                    
                    sh 'git push -u origin ${Gitbranch}'

            }
        }
        
         stage('Output')
        {
            steps{
                
                    
                       // sh 'aws eks update-kubeconfig --name ${ClusterName} --region ${ClusterRegion}'
                     
                       // sh 'echo ClusterName: ${ClusterName}, ClusterRegion: ${ClusterRegion}'
                        
                       // sh "aws eks describe-cluster --name ${ClusterName} --region ${ClusterRegion} | egrep  'endpoint|arn'"
                     sh 'terraform output'
                    
                         
                     
            }
                        
}
    
    
     
       

   

}
}