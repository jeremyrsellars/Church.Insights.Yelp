#install.packages("graph")
#library(graph)
new("graphAM")

source("https://bioconductor.org/biocLite.R")
biocLite("graph")
browseVignettes("graph")


set.seed(123)
x <- rnorm(26)
names(x) <- letters
library(stats)
d1 <- dist(x)
g1 <- new("distGraph", Dist=d1)
g1
?dist

mat <- rbind(c(0, 0, 1, 1),
             c(0, 0, 1, 1),
             c(1, 1, 0, 1),
             c(1, 1, 1, 0))
rownames(mat) <- colnames(mat) <- letters[1:4]
g1 <- new("graphAM", adjMat=mat)
stopifnot(identical(mat, as(g1, "matrix")), validObject(g1))

## now with weights:
mat[1,3] <- mat[3,1] <- 10
gw <- new("graphAM", adjMat=mat, values=list(weight=1))

## consistency check:
stopifnot(identical(mat, as(gw, "matrix")),
          validObject(gw),
          identical(gw, as(as(gw, "graphNEL"), "graphAM")))



library(jsonlite)
??jsonlite
setwd("C:/code/jeremy.sellars/coursera/dsscapstone/")


businesses <- stream_in(file("yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_business.json"))
businesses <- businesses[,c("business_id","name")]
businesses$business_id

users <- stream_in(file("yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_user.json"))
user <- users
