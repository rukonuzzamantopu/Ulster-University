# ============================================================================
# CMP330 Data Analytics - Assessment 2 (Pre-Assignment)
# ============================================================================

# Objective
# Predict the acceptability class of a car (`unacc`, `acc`, `good`, `vgood`) 
# from six categorical features, using five ML algorithms, and compare their 
# performance.

# ============================================================================
# 1. Data Preparation and Exploration
# ============================================================================

# 1.1 Load and clean

carDF <- read.csv("car.csv")
carDF <- data.frame(lapply(carDF, factor))

head(carDF)
str(carDF)

# 1.2 Frequency tables

# Target class counts
table(carDF$acceptability)

# Buying price vs acceptability
table(carDF$buying, carDF$acceptability)

# Extra: safety vs buying price - checking if cheaper cars are also less safe
table(carDF$buying, carDF$safety)

# Extra: doors vs lug_boot
table(carDF$doors, carDF$lug_boot)

# 1.3 Plots

library(ggplot2)

carDF$acceptability <- factor(carDF$acceptability,
                               levels = c("unacc", "acc", "good", "vgood"))

# Target distribution
ggplot(carDF, aes(x = acceptability)) +
  geom_bar(fill = "#3C6E71") +
  labs(title = "Class Distribution: Acceptability", x = "Acceptability", y = "Count") +
  theme_classic()

# By buying price
ggplot(carDF, aes(x = acceptability, fill = buying)) +
  geom_bar(position = "dodge") +
  scale_fill_manual(values = c("#D8973C", "#BD632F", "#7C3626", "#45322E")) +
  labs(title = "Acceptability vs Buying Price", x = "Acceptability", y = "Count") +
  theme_classic()

# Extra plot: by safety
ggplot(carDF, aes(x = acceptability, fill = safety)) +
  geom_bar(position = "dodge") +
  scale_fill_brewer(palette = "Paired") +
  labs(title = "Acceptability vs Safety Rating", x = "Acceptability", y = "Count") +
  theme_classic()

# 1.4 Mutual information

library(infotheo)

miMat <- mutinformation(carDF)
miMat

# Feature relevance to target
miMat[-7, 7]

library(reshape2)

miLong <- melt(miMat)
ggplot(miLong, aes(x = Var1, y = Var2, fill = value)) +
  geom_tile(color = "white") +
  scale_fill_gradient(low = "#E5F5E0", high = "#00441B") +
  labs(title = "Mutual Information Heatmap", x = "", y = "", fill = "MI") +
  theme(axis.text.x = element_text(angle = 45, hjust = 1))

# 1.5 Chi-squared tests

chisq.test(carDF$buying, carDF$acceptability)
chisq.test(carDF$buying, carDF$maint)

# Extra: maint vs acceptability
chisq.test(carDF$maint, carDF$acceptability)

# 1.6 Train/test split (fixed, per brief)

library(caTools)
set.seed(123)
split <- sample.split(carDF$acceptability, SplitRatio = 0.8)
trainDF <- subset(carDF, split == TRUE)
testDF  <- subset(carDF, split == FALSE)

summary(trainDF$acceptability)
summary(testDF$acceptability)
prop.table(table(trainDF$acceptability))
prop.table(table(testDF$acceptability))

# ============================================================================
# 2. Model Training and Evaluation
# ============================================================================

# 2.1 DECISION TREE (C5.0)

library(caret)
library(C50)

m.c50  <- C5.0(trainDF[-7], trainDF$acceptability)
p.c50  <- predict(m.c50, testDF)
confusionMatrix(p.c50, testDF$acceptability)

# 2.2 NAIVE BAYES

library(e1071)
set.seed(123)

m.nb  <- naiveBayes(trainDF[, -7], trainDF$acceptability)
p.nb  <- predict(m.nb, testDF)
confusionMatrix(p.nb, testDF$acceptability)

# 2.3 MULTINOMIAL LOGISTIC REGRESSION

library(nnet)

m.mnl  <- multinom(acceptability ~ ., data = trainDF)
p.mnl  <- predict(m.mnl, testDF)
confusionMatrix(p.mnl, testDF$acceptability)

# 2.4 SUPPORT VECTOR MACHINE (SVM)

library(kernlab)

m.svm1  <- ksvm(acceptability ~ ., data = trainDF, kernel = "vanilladot")
p.svm1  <- predict(m.svm1, testDF)
confusionMatrix(p.svm1, testDF$acceptability)

m.svm2  <- svm(acceptability ~ ., data = trainDF)
p.svm2  <- predict(m.svm2, testDF)
confusionMatrix(p.svm2, testDF$acceptability)

# 2.5 NEURAL NETWORK

# Reference: https://www.geeksforgeeks.org/r-language/neural-networks-using-the-r-nnet-package/
m.nn  <- nnet(acceptability ~ ., data = trainDF, size = 5)
p.nn  <- predict(m.nn, testDF, type = "class")
confusionMatrix(factor(p.nn), testDF$acceptability)

# ============================================================================
# 2.6 Cross-validated versions (caret)
# ============================================================================

# DECISION TREE - CV

ctl <- trainControl(method = "cv", number = 10)
set.seed(123)

m.c50.cv  <- train(trainDF[, -7], trainDF[, 7], method = "C5.0", trControl = ctl)
m.c50.cv
p.c50.cv <- predict(m.c50.cv, testDF)
confusionMatrix(p.c50.cv, testDF$acceptability)

# NAIVE BAYES - CV

ctl <- trainControl(method = "cv", number = 10)
set.seed(123)

m.nb.cv  <- train(trainDF[, -7], trainDF[, 7], method = "nb", trControl = ctl)
m.nb.cv
p.nb.cv <- predict(m.nb.cv, testDF)
confusionMatrix(p.nb.cv, testDF$acceptability)

# LOGISTIC REGRESSION - CV

ctl <- trainControl(method = "cv", number = 10)
set.seed(123)

m.mnl.cv  <- train(trainDF[, -7], trainDF[, 7], method = "multinom",
                    trControl = ctl, trace = FALSE)
m.mnl.cv
p.mnl.cv <- predict(m.mnl.cv, testDF)
confusionMatrix(p.mnl.cv, testDF$acceptability)

# NEURAL NETWORK - CV

ctl <- trainControl(method = "cv", number = 10)
set.seed(123)

m.nn.cv  <- train(trainDF[, -7], trainDF[, 7], method = "nnet",
                   trControl = ctl, trace = FALSE)
m.nn.cv
p.nn.cv <- predict(m.nn.cv, testDF)
confusionMatrix(p.nn.cv, testDF$acceptability)

# SVM - CV

set.seed(123)
m.svm.cv  <- ksvm(acceptability ~ ., data = trainDF, kernel = "vanilladot", cross = 10)
m.svm.cv
p.svm.cv <- predict(m.svm.cv, testDF)
confusionMatrix(p.svm.cv, testDF$acceptability)

# ============================================================================
# 2.7 Feature Selection
# ============================================================================

miTrain <- mutinformation(trainDF)
miTrain
miTrain[7, -7]

# Top 3 features by MI score: `persons`, `lug_boot`, `safety`.

# DECISION TREE - TOP 3 FEATURES

m.c50.fs  <- C5.0(trainDF[, 4:6], trainDF$acceptability)
p.c50.fs  <- predict(m.c50.fs, testDF)
confusionMatrix(p.c50.fs, testDF$acceptability)

# NAIVE BAYES - TOP 3 FEATURES

m.nb.fs  <- naiveBayes(trainDF[, 4:6], trainDF$acceptability)
p.nb.fs  <- predict(m.nb.fs, testDF)
confusionMatrix(p.nb.fs, testDF$acceptability)

# LOGISTIC REGRESSION - TOP 3 FEATURES

m.mnl.fs  <- multinom(acceptability ~ persons + lug_boot + safety, data = trainDF)
p.mnl.fs  <- predict(m.mnl.fs, testDF)
confusionMatrix(p.mnl.fs, testDF$acceptability)

# ============================================================================
# 3. Hyperparameter Tuning
# ============================================================================

# 3.1 NEURAL NETWORK - DECAY PARAMETER

ctl.tune <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
grid.nn  <- expand.grid(size = 5, decay = c(0, 0.001, 0.005, 0.01, 0.02, 0.05, 0.1))
set.seed(123)

m.nn.tuned <- train(trainDF[, -7], trainDF[, 7], method = "nnet",
                     trControl = ctl.tune, tuneGrid = grid.nn, trace = FALSE)
plot(m.nn.tuned)

# 3.2 DECISION TREE - TRIALS PARAMETER

ctl.tune  <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
grid.c50  <- expand.grid(model = "tree",
                          trials = c(1, 5, 10, 15, 20, 25, 30, 40, 50, 60, 80, 100),
                          winnow = FALSE)
set.seed(123)

m.c50.tuned <- train(trainDF[, -7], trainDF[, 7], method = "C5.0",
                      trControl = ctl.tune, tuneGrid = grid.c50)
plot(m.c50.tuned)

# ============================================================================
# Part 3. Presentation of Specified Results
# ============================================================================
# (To be finalised in the lab session once exact required figures are confirmed.)

# ============================================================================
# References
# ============================================================================
# - Kuhn, M. (2008) 'Building predictive models in R using the caret package', 
#   Journal of Statistical Software, 28(5), pp. 1-26.
# - UCI Machine Learning Repository (1997) Car Evaluation Data Set. Available 
#   at: https://archive.ics.uci.edu/dataset/19/car+evaluation (Accessed: [add date]).
# - Wickham, H. (2016) ggplot2: Elegant Graphics for Data Analysis. 
#   New York: Springer-Verlag.
