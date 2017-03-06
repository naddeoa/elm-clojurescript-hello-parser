#!/bin/bash

aws lambda invoke\
    --invocation-type RequestResponse \
    --region us-east-1 \
    --function-name parse \
    --payload '"fn: Int"'\
    output.txt

