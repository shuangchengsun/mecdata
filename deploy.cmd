docker build  -t mecdata_image .
docker run --name mecdata_container -d mecdata_image -v ~/logs:/logs