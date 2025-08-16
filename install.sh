#!/bin/bash
echo INSTALL SPINAL
git clone https://github.com/SpinalHDL/SpinalHDL.git
cd SpinalHDL
sbt clean publishLocal

# Scala some version 
curl -fLo cs https://git.io/coursier-cli-linux && chmod +x cs
./cs install scala:2.13.12