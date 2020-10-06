#### Experiments with Rundeck in k3s

Assuming you already installed the Token in your local config:

    $ export KUBECONFIG=~/.kube/config
    $ kubectl get nodes
    $ source <(kubectl completion bash)

Install Rundeck in Kubernetes

    $ kubectl create namespace hello-rundeck
    $ kubectl config set-context --current --namespace=hello-rundeck
    $ kubectl apply -k k3s/

Then  visit the  [URL](https://rundeck.localhost).
