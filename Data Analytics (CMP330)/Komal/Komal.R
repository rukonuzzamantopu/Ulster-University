# ============================================================================
# CMP330 Data Analytics - Assessment 2 (Pre-Assignment)
# ============================================================================

# Introduction
# This notebook classifies cars into four acceptability categories 
# (`unacc`, `acc`, `good`, `vgood`) using six categorical attributes. Five 
# machine learning approaches are trained and compared: decision trees, 
# naive Bayes, logistic regression, SVM, and neural networks.

# ============================================================================
# Section A: Exploratory Data Analysis
# ============================================================================

# A.1 Data import and preparation

carsDataset <- read.csv("car.csv")
carsDataset <- data.frame(lapply(carsDataset, factor))

head(carsDataset)
str(carsDataset)

# A.2 Frequency analysis

# Class distribution of the target variable
table(carsDataset$acceptability)

# Buying price against acceptability
table(carsDataset$buying, carsDataset$acceptability)

# Additional cross-tab: safety level against acceptability
table(carsDataset$safety, carsDataset$acceptability)

# Additional cross-tab: lug_boot against doors
table(carsDataset$lug_boot, carsDataset$doors)

# A.3 Graphical summaries

library(ggplot2)

carsDataset$acceptability <- factor(carsDataset$acceptability,
                                     levels = c("unacc", "acc", "good", "vgood"))

# Distribution of target variable
ggplot(carsDataset, aes(x = acceptability)) +
  geom_bar(fill = "#2C7FB8") +
  labs(title = "Distribution of Acceptability Classes",
       x = "Acceptability", y = "Frequency") +
  theme_light()

# Acceptability by buying price
ggplot(carsDataset, aes(x = acceptability, fill = buying)) +
  geom_bar(position = "dodge") +
  scale_fill_viridis_d() +
  labs(title = "Acceptability Distribution by Buying Price",
       x = "Acceptability", y = "Frequency") +
  theme_light()

# Additional plot: acceptability by maintenance cost
ggplot(carsDataset, aes(x = acceptability, fill = maint)) +
  geom_bar(position = "dodge") +
  scale_fill_brewer(palette = "Dark2") +
  labs(title = "Acceptability Distribution by Maintenance Cost",
       x = "Acceptability", y = "Frequency") +
  theme_light()

# A.4 Mutual information analysis

library(infotheo)

mutualInfoMatrix <- mutinformation(carsDataset)
mutualInfoMatrix

# Mutual information of each predictor with the target column
mutualInfoMatrix[-7, 7]

library(reshape2)

mutualInfoLong <- melt(mutualInfoMatrix)
ggplot(mutualInfoLong, aes(x = Var1, y = Var2, fill = value)) +
  geom_tile(color = "grey90") +
  scale_fill_gradient(low = "#EFF3FF", high = "#08306B") +
  labs(title = "Mutual Information Matrix Heatmap", x = "", y = "", fill = "MI") +
  theme(axis.text.x = element_text(angle = 45, hjust = 1))

# A.5 Chi-squared independence tests

chisq.test(carsDataset$buying, carsDataset$acceptability)
chisq.test(carsDataset$buying, carsDataset$maint)

# Additional test: doors against persons
chisq.test(carsDataset$doors, carsDataset$persons)

# A.6 Train-test partitioning

library(caTools)
set.seed(123)
split <- sample.split(carsDataset$acceptability, SplitRatio = 0.8)
trainingSet <- subset(carsDataset, split == TRUE)
testingSet  <- subset(carsDataset, split == FALSE)

summary(trainingSet$acceptability)
summary(testingSet$acceptability)
prop.table(table(trainingSet$acceptability))
prop.table(table(testingSet$acceptability))

# ============================================================================
# Section B: Machine Learning Model Development
# ============================================================================

# DECISION TREE CLASSIFIER (C5.0)

library(caret)
library(C50)

decisionTreeModel <- C5.0(trainingSet[-7], trainingSet$acceptability)
decisionTreePred  <- predict(decisionTreeModel, testingSet)
confusionMatrix(decisionTreePred, testingSet$acceptability)

# NAIVE BAYES CLASSIFIER

library(e1071)
set.seed(123)

naiveBayesModel <- naiveBayes(trainingSet[, -7], trainingSet$acceptability)
naiveBayesPred  <- predict(naiveBayesModel, testingSet)
confusionMatrix(naiveBayesPred, testingSet$acceptability)

# MULTINOMIAL LOGISTIC REGRESSION

library(nnet)

logisticRegModel <- multinom(acceptability ~ ., data = trainingSet)
logisticRegPred  <- predict(logisticRegModel, testingSet)
confusionMatrix(logisticRegPred, testingSet$acceptability)

# SUPPORT VECTOR MACHINE

library(kernlab)

svmModelKsvm <- ksvm(acceptability ~ ., data = trainingSet, kernel = "vanilladot")
svmPredKsvm  <- predict(svmModelKsvm, testingSet)
confusionMatrix(svmPredKsvm, testingSet$acceptability)

svmModelE1071 <- svm(acceptability ~ ., data = trainingSet)
svmPredE1071  <- predict(svmModelE1071, testingSet)
confusionMatrix(svmPredE1071, testingSet$acceptability)

# NEURAL NETWORK

# Reference: https://www.geeksforgeeks.org/r-language/neural-networks-using-the-r-nnet-package/
neuralNetModel <- nnet(acceptability ~ ., data = trainingSet, size = 5)
neuralNetPred  <- predict(neuralNetModel, testingSet, type = "class")
confusionMatrix(factor(neuralNetPred), testingSet$acceptability)

# ============================================================================
# Cross-Validated Models
# ============================================================================

# DECISION TREE WITH CROSS-VALIDATION

crossValCtrl <- trainControl(method = "cv", number = 10)
set.seed(123)

decisionTreeCvModel <- train(trainingSet[, -7], trainingSet[, 7], method = "C5.0",
                              trControl = crossValCtrl)
decisionTreeCvModel
decisionTreeCvPred <- predict(decisionTreeCvModel, testingSet)
confusionMatrix(decisionTreeCvPred, testingSet$acceptability)

# NAIVE BAYES WITH CROSS-VALIDATION

crossValCtrl <- trainControl(method = "cv", number = 10)
set.seed(123)

naiveBayesCvModel <- train(trainingSet[, -7], trainingSet[, 7], method = "nb",
                            trControl = crossValCtrl)
naiveBayesCvModel
naiveBayesCvPred <- predict(naiveBayesCvModel, testingSet)
confusionMatrix(naiveBayesCvPred, testingSet$acceptability)

# LOGISTIC REGRESSION WITH CROSS-VALIDATION

crossValCtrl <- trainControl(method = "cv", number = 10)
set.seed(123)

logisticRegCvModel <- train(trainingSet[, -7], trainingSet[, 7], method = "multinom",
                             trControl = crossValCtrl, trace = FALSE)
logisticRegCvModel
logisticRegCvPred <- predict(logisticRegCvModel, testingSet)
confusionMatrix(logisticRegCvPred, testingSet$acceptability)

# NEURAL NETWORK WITH CROSS-VALIDATION

crossValCtrl <- trainControl(method = "cv", number = 10)
set.seed(123)

neuralNetCvModel <- train(trainingSet[, -7], trainingSet[, 7], method = "nnet",
                           trControl = crossValCtrl, trace = FALSE)
neuralNetCvModel
neuralNetCvPred <- predict(neuralNetCvModel, testingSet)
confusionMatrix(neuralNetCvPred, testingSet$acceptability)

# SVM WITH CROSS-VALIDATION

set.seed(123)
svmCvModel <- ksvm(acceptability ~ ., data = trainingSet, kernel = "vanilladot", cross = 10)
svmCvModel
svmCvPred <- predict(svmCvModel, testingSet)
confusionMatrix(svmCvPred, testingSet$acceptability)

# ============================================================================
# Feature Selection via Mutual Information
# ============================================================================

mutualInfoTrain <- mutinformation(trainingSet)
mutualInfoTrain
mutualInfoTrain[7, -7]

# Based on the scores above, `persons`, `lug_boot`, and `safety` are selected 
# as the top three predictive features.

# DECISION TREE WITH SELECTED FEATURES

decisionTreeFsModel <- C5.0(trainingSet[, 4:6], trainingSet$acceptability)
decisionTreeFsPred  <- predict(decisionTreeFsModel, testingSet)
confusionMatrix(decisionTreeFsPred, testingSet$acceptability)

# NAIVE BAYES WITH SELECTED FEATURES

naiveBayesFsModel <- naiveBayes(trainingSet[, 4:6], trainingSet$acceptability)
naiveBayesFsPred  <- predict(naiveBayesFsModel, testingSet)
confusionMatrix(naiveBayesFsPred, testingSet$acceptability)

# LOGISTIC REGRESSION WITH SELECTED FEATURES

logisticRegFsModel <- multinom(acceptability ~ persons + lug_boot + safety, data = trainingSet)
logisticRegFsPred  <- predict(logisticRegFsModel, testingSet)
confusionMatrix(logisticRegFsPred, testingSet$acceptability)

# ============================================================================
# Section C: Hyperparameter Tuning
# ============================================================================

# NEURAL NETWORK DECAY TUNING

tuningCtrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
neuralNetGrid <- expand.grid(size = 5, decay = c(0, 0.001, 0.005, 0.01, 0.02, 0.05, 0.1))
set.seed(123)

neuralNetTuned <- train(trainingSet[, -7], trainingSet[, 7], method = "nnet",
                         trControl = tuningCtrl, tuneGrid = neuralNetGrid, trace = FALSE)
plot(neuralNetTuned)

# DECISION TREE TRIALS TUNING

tuningCtrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
decisionTreeGrid <- expand.grid(model = "tree",
                                 trials = c(1, 5, 10, 15, 20, 25, 30, 40, 50, 60, 80, 100),
                                 winnow = FALSE)
set.seed(123)

decisionTreeTuned <- train(trainingSet[, -7], trainingSet[, 7], method = "C5.0",
                            trControl = tuningCtrl, tuneGrid = decisionTreeGrid)
plot(decisionTreeTuned)

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
