# ============================================================================
# CMP330 Data Analytics - Assessment 2 (Pre-Assignment)
# ============================================================================

# What I'm doing in this notebook
# Using the Car Evaluation dataset to predict how acceptable a car is 
# (`unacc`, `acc`, `good`, `vgood`), based on six features. I'll do the EDA 
# first, then run five ML algorithms and compare their test accuracy, then try 
# to improve things with feature selection and tuning.

# ============================================================================
# Part 1 - Exploring the Dataset
# ============================================================================

# 1.1 Loading
# Everything needs to be a factor since none of the columns are actually 
# numeric - they're all categories like "low", "high", "5more", etc.

autoTbl <- read.csv("car.csv")
autoTbl <- data.frame(lapply(autoTbl, factor))

head(autoTbl)
str(autoTbl)

# 1.2 Counting things up

# how many of each class - not evenly split at all, "unacc" is way ahead
table(autoTbl$acceptability)

table(autoTbl$buying, autoTbl$acceptability)

# checking if maintenance cost tells a similar story to buying price
table(autoTbl$maint, autoTbl$acceptability)

# lug_boot vs doors, just out of curiosity about how these two relate
table(autoTbl$lug_boot, autoTbl$doors)

# 1.3 Plotting
# Tried to use a mix of chart types instead of only bar charts, so I could 
# actually compare a few different ways of showing the same relationships.

library(ggplot2)

autoTbl$acceptability <- factor(autoTbl$acceptability,
                                 levels = c("unacc", "acc", "good", "vgood"))

# simple count of each acceptability class
ggplot(autoTbl, aes(x = acceptability)) +
  geom_bar(fill = "#6A4C93") +
  labs(title = "How many cars fall into each class?", x = "Acceptability", y = "Count") +
  theme_light()

# grouped bar chart - buying price broken down by class
ggplot(autoTbl, aes(x = buying, fill = acceptability)) +
  geom_bar(position = "dodge") +
  scale_fill_viridis_d(option = "plasma") +
  labs(title = "Buying Price Grouped by Acceptability", x = "Buying Price", y = "Count") +
  theme_light()

# a coord_flip horizontal bar chart for safety, just for a bit of variety
ggplot(autoTbl, aes(x = safety, fill = acceptability)) +
  geom_bar(position = "fill") +
  coord_flip() +
  scale_fill_manual(values = c("#264653", "#2A9D8F", "#E9C46A", "#E76F51")) +
  labs(title = "Proportion of Acceptability within each Safety Level",
       x = "Safety", y = "Proportion") +
  theme_light()

# 1.4 Mutual information
# Correlation doesn't apply to categorical data, so mutual information is 
# used here instead - basically measures how much one variable tells you 
# about another.

library(infotheo)

miScores <- mutinformation(autoTbl)
miScores

# sorted so I can see straight away which features matter most for the target
sort(miScores[-7, 7], decreasing = TRUE)

# For the visualisation I used `ggcorrplot`, which is normally meant for 
# correlation matrices but also works on a plain numeric matrix like this one 
# - I just had to turn off the significance testing since that assumes a 
# correlation coefficient in [-1, 1], which mutual information isn't.

library(ggcorrplot)

ggcorrplot(miScores, method = "square", lab = TRUE, lab_size = 3,
           colors = c("#F7FBFF", "#6BAED6", "#08306B"),
           title = "Mutual Information Between All Variables",
           ggtheme = ggplot2::theme_minimal())

# 1.5 Chi-squared tests

chisq.test(autoTbl$buying, autoTbl$acceptability)
chisq.test(autoTbl$buying, autoTbl$maint)

# lug_boot vs acceptability - wanted to see if boot size actually matters
# statistically or if it's mostly noise
chisq.test(autoTbl$lug_boot, autoTbl$acceptability)

# 1.6 Splitting the data
# This exact code is specified in the brief, so it's left unchanged (same 
# seed and ratio needed for everyone's results to be comparable).

library(caTools)
set.seed(123)
split <- sample.split(autoTbl$acceptability, SplitRatio = 0.8)
trainRows <- subset(autoTbl, split == TRUE)
testRows  <- subset(autoTbl, split == FALSE)

summary(trainRows$acceptability)
summary(testRows$acceptability)
prop.table(table(trainRows$acceptability))
prop.table(table(testRows$acceptability))

# ============================================================================
# Part 2a - Training the Models
# ============================================================================

# DECISION TREE (C5.0)

library(caret)
library(C50)

treeMod  <- C5.0(trainRows[-7], trainRows$acceptability)
treePred <- predict(treeMod, testRows)
confusionMatrix(treePred, testRows$acceptability)

# NAIVE BAYES

library(e1071)
set.seed(123)

nbMod  <- naiveBayes(trainRows[, -7], trainRows$acceptability)
nbPred <- predict(nbMod, testRows)
confusionMatrix(nbPred, testRows$acceptability)

# LOGISTIC REGRESSION (MULTINOMIAL)

library(nnet)

logRegMod  <- multinom(acceptability ~ ., data = trainRows)
logRegPred <- predict(logRegMod, testRows)
confusionMatrix(logRegPred, testRows$acceptability)

# SUPPORT VECTOR MACHINE (SVM)

library(kernlab)

svmMod_a  <- ksvm(acceptability ~ ., data = trainRows, kernel = "vanilladot")
svmPred_a <- predict(svmMod_a, testRows)
confusionMatrix(svmPred_a, testRows$acceptability)

# second SVM using e1071's default kernel, mostly just to see if it beats
# the linear one above
svmMod_b  <- svm(acceptability ~ ., data = trainRows)
svmPred_b <- predict(svmMod_b, testRows)
confusionMatrix(svmPred_b, testRows$acceptability)

# NEURAL NETWORK

# approach adapted from:
# https://www.geeksforgeeks.org/r-language/neural-networks-using-the-r-nnet-package/
netMod  <- nnet(acceptability ~ ., data = trainRows, size = 5)
netPred <- predict(netMod, testRows, type = "class")
confusionMatrix(factor(netPred), testRows$acceptability)

# ============================================================================
# Cross-validated models
# ============================================================================
# Re-running each algorithm with 10-fold CV through `caret::train()`, which 
# gives a more reliable accuracy estimate than a single train/test split.

# DECISION TREE - CV

foldSetup <- trainControl(method = "cv", number = 10)
set.seed(123)

treeMod_cv  <- train(trainRows[, -7], trainRows[, 7], method = "C5.0", trControl = foldSetup)
treeMod_cv
treePred_cv <- predict(treeMod_cv, testRows)
confusionMatrix(treePred_cv, testRows$acceptability)

# NAIVE BAYES - CV

foldSetup <- trainControl(method = "cv", number = 10)
set.seed(123)

nbMod_cv  <- train(trainRows[, -7], trainRows[, 7], method = "nb", trControl = foldSetup)
nbMod_cv
nbPred_cv <- predict(nbMod_cv, testRows)
confusionMatrix(nbPred_cv, testRows$acceptability)

# LOGISTIC REGRESSION - CV

foldSetup <- trainControl(method = "cv", number = 10)
set.seed(123)

logRegMod_cv  <- train(trainRows[, -7], trainRows[, 7], method = "multinom",
                        trControl = foldSetup, trace = FALSE)
logRegMod_cv
logRegPred_cv <- predict(logRegMod_cv, testRows)
confusionMatrix(logRegPred_cv, testRows$acceptability)

# NEURAL NETWORK - CV

foldSetup <- trainControl(method = "cv", number = 10)
set.seed(123)

netMod_cv  <- train(trainRows[, -7], trainRows[, 7], method = "nnet",
                     trControl = foldSetup, trace = FALSE)
netMod_cv
netPred_cv <- predict(netMod_cv, testRows)
confusionMatrix(netPred_cv, testRows$acceptability)

# SVM - CV

set.seed(123)
svmMod_cv  <- ksvm(acceptability ~ ., data = trainRows, kernel = "vanilladot", cross = 10)
svmMod_cv
svmPred_cv <- predict(svmMod_cv, testRows)
confusionMatrix(svmPred_cv, testRows$acceptability)

# ============================================================================
# Feature selection - comparing different values of x
# ============================================================================
# Same idea as before but ranked the features first and then tested top-2, 
# top-4 and top-6 (basically all of them) to see how much of a difference 
# dropping weak features actually makes. Used the decision tree as the 
# quick test model since it trains almost instantly.

miTrainScores <- mutinformation(trainRows)
featureRanking <- names(sort(miTrainScores[7, -7], decreasing = TRUE))
featureRanking

xValues <- c(2, 4, 6)
fsAccuracy <- data.frame(x = integer(), accuracy = numeric())

for (x in xValues) {
  cols_x <- featureRanking[1:x]
  
  mod_x  <- C5.0(trainRows[, cols_x], trainRows$acceptability)
  pred_x <- predict(mod_x, testRows)
  acc_x  <- confusionMatrix(pred_x, testRows$acceptability)$overall["Accuracy"]
  
  fsAccuracy <- rbind(fsAccuracy, data.frame(x = x, accuracy = acc_x))
}

fsAccuracy

ggplot(fsAccuracy, aes(x = factor(x), y = accuracy)) +
  geom_col(fill = "#588157", width = 0.5) +
  geom_text(aes(label = round(accuracy, 3)), vjust = -0.5) +
  labs(title = "Test Accuracy for Different Numbers of Selected Features",
       x = "Number of top features (x)", y = "Accuracy") +
  ylim(0, 1) +
  theme_light()

# Also checking Naive Bayes and logistic regression with just the top 4 
# features (a middle-ground choice based on the chart above):

top4Cols <- featureRanking[1:4]

nbMod_fs  <- naiveBayes(trainRows[, top4Cols], trainRows$acceptability)
nbPred_fs <- predict(nbMod_fs, testRows)
confusionMatrix(nbPred_fs, testRows$acceptability)

fsFormula <- as.formula(paste("acceptability ~", paste(top4Cols, collapse = " + ")))
logRegMod_fs  <- multinom(fsFormula, data = trainRows)
logRegPred_fs <- predict(logRegMod_fs, testRows)
confusionMatrix(logRegPred_fs, testRows$acceptability)

# ============================================================================
# Part 2b - Tuning for Better Accuracy
# ============================================================================

# NEURAL NETWORK - DECAY
# Went with a slightly different (and wider) set of decay values compared to 
# the lab example, plus tried two network sizes instead of just one, to see 
# if a bigger hidden layer changes which decay value works best.

tuneSetup <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
netGrid <- expand.grid(size = c(4, 6),
                        decay = c(0, 0.002, 0.008, 0.02, 0.04, 0.08, 0.2))
set.seed(123)

netTuned <- train(trainRows[, -7], trainRows[, 7], method = "nnet",
                   trControl = tuneSetup, tuneGrid = netGrid, trace = FALSE)
plot(netTuned)

# DECISION TREE - TRIALS
# Used a slightly different spread of trial counts than the standard lab 
# example, spacing them out a bit more unevenly to check both the low end and 
# high end of the range.

tuneSetup <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
treeGrid <- expand.grid(model = "tree",
                         trials = c(1, 3, 8, 20, 35, 55, 75, 100),
                         winnow = FALSE)
set.seed(123)

treeTuned <- train(trainRows[, -7], trainRows[, 7], method = "C5.0",
                    trControl = tuneSetup, tuneGrid = treeGrid)
plot(treeTuned)

# ============================================================================
# Part 3. Presentation of Specified Results
# ============================================================================
# (To be finalised in the lab session once the exact figures required are 
# confirmed - the feature-selection bar chart above and the two tuning plots 
# are good candidates for this section.)

# ============================================================================
# References
# ============================================================================
# - Kuhn, M. (2008) 'Building predictive models in R using the caret package', 
#   Journal of Statistical Software, 28(5), pp. 1-26.
# - Kassambara, A. (2019) ggcorrplot: Visualization of a Correlation Matrix 
#   using ggplot2. R package.
# - UCI Machine Learning Repository (1997) Car Evaluation Data Set. Available 
#   at: https://archive.ics.uci.edu/dataset/19/car+evaluation (Accessed: [add date]).
# - Wickham, H. (2016) ggplot2: Elegant Graphics for Data Analysis. 
#   New York: Springer-Verlag.
