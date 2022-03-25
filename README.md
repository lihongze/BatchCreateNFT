## BatchCreateNFT
The NFT assembly tool developed by java can generate NFT in batches by arranging and combining pictures according to each layer.
## Features
* Support multi-threaded parallel generation of pictures (single thread generation of pictures is too slow)
* Support to generate metadata data at the same time
* Support batch clearing the corresponding metadata according to the deleted image address
* Support batch rearrange image id
* Support batch reset image size
## Instructions
The NFT project generation step, of course, is to first draw each layer by the artist, and then generate different series according to different layer combinations, but due to the randomness of the system generation,
It will cause the generated art pictures to have defective products. At this time, the artist needs to filter the generated series of combinations, and finally start all the filtered series of pictures from id=1.
Incrementally rename to desired number, then aggregate all series as nft final product portfolio
## To be developed
* Randomly rearrange ids in batches according to the total number of result sets
* The ipfs tutorial for image uploading will be gradually updated in the future to realize the content of the smart contract on the chain