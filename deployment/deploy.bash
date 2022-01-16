cd ..

mvn clean install

docker build -t fp -f deployment/dockerfile .
docker tag fp azch97/fp:latest
docker push azch97/fp:latest


scp -r ./deployment/kubernetes server:~/produce-forex-rate
ssh server 'kubectl replace --force -f ~/produce-forex-rate/deployment.yml'

