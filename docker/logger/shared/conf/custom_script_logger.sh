#!/bin/bash
KEYSTORE="$JAVA_HOME/lib/security/cacerts"
KEYSTORE_PASS="changeit"
KEYTOOL="$JAVA_HOME/bin/keytool"
ENROLLCERTS="/var/opt/nextlabs/logger/shared/enroll"

for cerfile in "$ENROLLCERTS"/*.pem
do
  if [ -f "$cerfile" ]
  then
    filename=$(basename "$cerfile")
    filename="${filename%.*}"
    echo "Checking $KEYSTORE to add $cerfile with alias $filename"

    "$KEYTOOL" -keystore "$KEYSTORE" -storepass "$KEYSTORE_PASS" -list -alias "$filename" > /dev/null 2>&1
    if [ $? -eq 0 ]
    then
        echo "Alias of $filename already found, skipping insertion into $KEYSTORE"
    else
        echo "Inserting into $KEYSTORE under alias of $filename"
        "$KEYTOOL" -noprompt -keystore "$KEYSTORE" -storepass "$KEYSTORE_PASS" -import -trustcacerts -alias "$filename" -file "$cerfile"
    fi
  fi
done
