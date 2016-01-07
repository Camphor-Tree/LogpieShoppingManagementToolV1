sudo service tomcat7 stop
cd /var/lib/tomcat7/webapps
sudo rm -rf ROOT ROOT.war
sudo rm -rf LogpieShopping
sudo mv /var/lib/tomcat7/webapps/LogpieShopping.war /var/lib/tomcat7/webapps/ROOT.war
sudo service tomcat7 start