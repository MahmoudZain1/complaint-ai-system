#!/bin/bash

set -e
cd "$(dirname "$0")/../../../../../../.."

mvn clean generate-sources -Pgenerate-swagger

echo "Generation completed successfully"
