// pipeline steps
// 1. pull terraform script from git
// 2. Install aws CLI and aws configure with credentials.
// 3. Install terraform and terraform init, plan, apply


pipeline{
    agent any
    
    stages{
        stage('Checkout'){
            steps {
                // 1.Pull terraform scripts from git
                    git url: 'https://github.com/sagarkivale101/terraformEKS.git', branch : "master"
                    sh 'ls'
                    //cd 'eks-terraform'
                    //sh 'ls'
            }
        }
        
        stage('aws config'){
 
            steps{
                // 1.Install aws cli 
                  sh "rm -rf awscliv2.zip"
                  sh "rm -rf aws"
                  sh 'curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"'
                  sh "unzip awscliv2.zip"
                  sh 'apt install sudo'
               //   sh "sudo ./aws/install"
                  sh "aws --version"
                // 2.Login to aws using credentials 
                
                  sh "aws configure set aws_access_key_id 'awsID' && aws configure set aws_secret_access_key 'awsSecretKey' && aws configure set region 'us-east-1'"
                
            }
        }
        stage('terraform apply'){
    steps {
        
    sh "ls"
    // 1. install necessary packages depending upon 'O.S.' on which jenkins is running
           
        sh 'apt-get update'
        sh 'apt install sudo'
        sh 'apt-get install wget'
        sh "sudo apt install wget zip python3-pip -y"
        sh "curl -o terraform.zip https://releases.hashicorp.com/terraform/1.2.3/terraform_1.2.3_linux_amd64.zip"
        sh "unzip terraform.zip"
        sh "sudo mv terraform /usr/bin"
        sh "rm -rf terraform.zip"
        
        // 2. terrafrom init, plan & apply
        sh 'terraform version'
        sh 'terraform init -no-color'
        sh 'terraform plan -no-color'
        // sh 'terraform apply -auto-approve'
         sh 'terraform destroy -auto-approve'
        
    

    }
}



}
}