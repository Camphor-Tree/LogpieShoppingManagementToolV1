cd /var/lib/tomcat7/webapps/website
sudo rm -rf ROOT ROOT.war
sudo mv /var/lib/tomcat7/webapps/website/LogpieShoppingWebsite.war /var/lib/tomcat7/webapps/website/ROOT.war
sudo mv -rf LogpieShoppingWebsite
sudo service tomcat7 start