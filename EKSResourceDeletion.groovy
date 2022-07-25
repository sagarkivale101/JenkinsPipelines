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
                
                
               
                  //  git url: '${GitHubUrl}', branch : "${GitHubBranch}
            sh       'git clone ${GitHubUrl} --single-branch --branch ${GitHubBranch}'
                    sh 'ls -a'
                   
            }
        }
        
        stage('AWS Configuration'){
 
            steps{
                  sh 'apt-get update'
                  sh 'apt install sudo'
                  sh 'apt-get install wget'
               
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
        dir('test'){
        sh 'terraform version'
         sh 'terraform init -no-color'
     
        sh 'terraform destroy -auto-approve -no-color'
    }
    }
    }
    
}
}