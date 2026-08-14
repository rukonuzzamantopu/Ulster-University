## ============================================================
## CMP330 Data Analytics - Assessment 2 (Pre-Assignment)
## Author: MD Ali Hasan Jidan (B01011849)
## ============================================================
## This script contains the solution to the CMP330 Assessment 2
## pre-assignment. The goal is to classify cars according to their
## acceptability (unacc, acc, good, vgood) based on six categorical
## features (buying, maint, doors, persons, lug_boot, safety) using
## several machine learning algorithms.
## ============================================================


## ============================================================
## PART 1: EXPLORATORY DATA ANALYSIS AND DATA PREPARATION
## ============================================================

## ---- 1.1 Loading the data ----
# The dataset is read in and every column is converted to a factor,
# since all variables (including the target) are categorical rather
# than numeric.

car_data <- read.csv("car.csv")
car_data <- data.frame(lapply(car_data, factor))

head(car_data)
str(car_data)


## ---- 1.2 Basic frequency counts ----
# Before modelling, it's useful to see how the classes are distributed
# and how individual features relate to the target label.

# How many cars fall into each acceptability class?
table(car_data$acceptability)

# Cross-tabulation: buying price vs acceptability
table(car_data$buying, car_data$acceptability)

# Cross-tabulation: safety vs acceptability (an extra one added, since
# safety is intuitively a strong predictor of acceptability)
table(car_data$safety, car_data$acceptability)

summary(car_data)


## ---- 1.3 Visualising the distributions ----
# ggplot2 is used to visualise the target distribution on its own, and
# then broken down by other features to get a first impression of which
# variables might be useful predictors.

library(ggplot2)

# Fix the ordering of the acceptability levels so charts read logically
car_data$acceptability <- factor(car_data$acceptability,
                                 levels = c("unacc", "acc", "good", "vgood"))

# Overall distribution of the target class
ggplot(car_data, aes(x = acceptability)) +
  geom_bar(fill = "steelblue") +
  labs(title = "Distribution of Car Acceptability",
       x = "Acceptability", y = "Count")

# Acceptability broken down by buying price
ggplot(car_data, aes(x = acceptability, fill = buying)) +
  geom_bar(position = "dodge") +
  labs(title = "Acceptability by Buying Price",
       x = "Acceptability", y = "Count")

# Extra plot: acceptability broken down by safety level
ggplot(car_data, aes(x = acceptability, fill = safety)) +
  geom_bar(position = "dodge") +
  labs(title = "Acceptability by Safety Level",
       x = "Acceptability", y = "Count")


## ---- 1.4 Mutual information between variables ----
# Since none of the variables are numeric, Pearson correlation isn't
# appropriate. Instead, mutual information from the infotheo package is
# used, which measures how much knowing one categorical variable
# reduces uncertainty about another.

# install.packages("infotheo")
library(infotheo)

mi_matrix <- mutinformation(car_data)
mi_matrix

# Mutual information of every feature with the target (acceptability is
# column 7)
mi_matrix[-7, 7]

# Visualised as a simple heatmap so the strongest relationships are
# easier to spot at a glance
library(reshape2)

mi_melted <- melt(mi_matrix)
ggplot(mi_melted, aes(x = Var1, y = Var2, fill = value)) +
  geom_tile() +
  scale_fill_gradient(low = "white", high = "darkred") +
  labs(title = "Mutual Information Heatmap", x = "", y = "", fill = "MI") +
  theme(axis.text.x = element_text(angle = 45, hjust = 1))


## ---- 1.5 Chi-squared tests of independence ----
# A chi-squared test checks whether two categorical variables are
# statistically independent.

chisq.test(car_data$buying, car_data$acceptability)
chisq.test(car_data$buying, car_data$maint)

# Extra test: is safety independent of acceptability?
chisq.test(car_data$safety, car_data$acceptability)


## ---- 1.6 Train/test split ----
# As specified in the assignment brief, the data is split 80/20 using a
# fixed seed so that results are reproducible and comparable across
# students. This exact code must be used as given.

library(caTools)
set.seed(123)
split <- sample.split(car_data$acceptability, SplitRatio = 0.8)
car_train <- subset(car_data, split == TRUE)
car_test  <- subset(car_data, split == FALSE)

summary(car_train$acceptability)
summary(car_test$acceptability)
prop.table(table(car_train$acceptability))
prop.table(table(car_test$acceptability))


## ============================================================
## PART 2a: APPLICATION OF MACHINE LEARNING ALGORITHMS
## ============================================================
# Each of the five required algorithms is trained on car_train and
# evaluated on car_test using a confusion matrix (accuracy, kappa,
# per-class sensitivity/specificity, etc.).

## ---- Decision Tree (C5.0) ----
library(caret)
library(C50)

tree_model <- C5.0(car_train[-7], car_train$acceptability)
tree_pred  <- predict(tree_model, car_test)
confusionMatrix(tree_pred, car_test$acceptability)


## ---- Naive Bayes ----
library(e1071)
set.seed(123)

nb_model <- naiveBayes(car_train[, -7], car_train$acceptability)
nb_pred  <- predict(nb_model, car_test)
confusionMatrix(nb_pred, car_test$acceptability)


## ---- Multinomial Logistic Regression ----
library(nnet)

logreg_model <- multinom(acceptability ~ ., data = car_train)
logreg_pred  <- predict(logreg_model, car_test)
confusionMatrix(logreg_pred, car_test$acceptability)


## ---- Support Vector Machine ----
library(kernlab)

svm_model_ksvm <- ksvm(acceptability ~ ., data = car_train, kernel = "vanilladot")
svm_pred_ksvm  <- predict(svm_model_ksvm, car_test)
confusionMatrix(svm_pred_ksvm, car_test$acceptability)

# Alternative SVM implementation using e1071, for comparison
svm_model_e1071 <- svm(acceptability ~ ., data = car_train)
svm_pred_e1071  <- predict(svm_model_e1071, car_test)
confusionMatrix(svm_pred_e1071, car_test$acceptability)


## ---- Neural Network ----
# Reference: https://www.geeksforgeeks.org/r-language/neural-networks-using-the-r-nnet-package/
nn_model <- nnet(acceptability ~ ., data = car_train, size = 5)
nn_pred  <- predict(nn_model, car_test, type = "class")
confusionMatrix(factor(nn_pred), car_test$acceptability)


## ---- Using caret::train() with cross-validation ----
# For each algorithm, 10-fold cross-validation is used to tune
# hyperparameters automatically via the train() function from caret.

## Decision Tree with CV
ctrl <- trainControl(method = "cv", number = 10)
set.seed(123)

tree_cv_model <- train(car_train[, -7], car_train[, 7], method = "C5.0", trControl = ctrl)
tree_cv_model
tree_cv_pred <- predict(tree_cv_model, car_test)
confusionMatrix(tree_cv_pred, car_test$acceptability)

## Naive Bayes with CV
ctrl <- trainControl(method = "cv", number = 10)
set.seed(123)

nb_cv_model <- train(car_train[, -7], car_train[, 7], method = "nb", trControl = ctrl)
nb_cv_model
nb_cv_pred <- predict(nb_cv_model, car_test)
confusionMatrix(nb_cv_pred, car_test$acceptability)

## Logistic Regression with CV
ctrl <- trainControl(method = "cv", number = 10)
set.seed(123)

logreg_cv_model <- train(car_train[, -7], car_train[, 7], method = "multinom",
                         trControl = ctrl, trace = FALSE)
logreg_cv_model
logreg_cv_pred <- predict(logreg_cv_model, car_test)
confusionMatrix(logreg_cv_pred, car_test$acceptability)

## Neural Network with CV
ctrl <- trainControl(method = "cv", number = 10)
set.seed(123)

nn_cv_model <- train(car_train[, -7], car_train[, 7], method = "nnet",
                     trControl = ctrl, trace = FALSE)
nn_cv_model
nn_cv_pred <- predict(nn_cv_model, car_test)
confusionMatrix(nn_cv_pred, car_test$acceptability)

## SVM with CV
set.seed(123)
svm_cv_model <- ksvm(acceptability ~ ., data = car_train, kernel = "vanilladot", cross = 10)
svm_cv_model
svm_cv_pred <- predict(svm_cv_model, car_test)
confusionMatrix(svm_cv_pred, car_test$acceptability)


## ---- Feature selection using mutual information ----
# The mutual information between each feature and the target (computed
# on the training set only, to avoid data leakage) is used to rank
# features. The top three -- persons, lug_boot, safety -- are then used
# to retrain simplified models.

mi_train <- mutinformation(car_train)
mi_train
mi_train[7, -7]

## Decision Tree with selected features
tree_fs_model <- C5.0(car_train[, 4:6], car_train$acceptability)
tree_fs_pred  <- predict(tree_fs_model, car_test)
confusionMatrix(tree_fs_pred, car_test$acceptability)

## Naive Bayes with selected features
nb_fs_model <- naiveBayes(car_train[, 4:6], car_train$acceptability)
nb_fs_pred  <- predict(nb_fs_model, car_test)
confusionMatrix(nb_fs_pred, car_test$acceptability)

## Logistic Regression with selected features
logreg_fs_model <- multinom(acceptability ~ persons + lug_boot + safety, data = car_train)
logreg_fs_pred  <- predict(logreg_fs_model, car_test)
confusionMatrix(logreg_fs_pred, car_test$acceptability)


## ============================================================
## PART 2b: IMPROVING MODEL PERFORMANCE VIA HYPERPARAMETER TUNING
## ============================================================

## ---- Tuning the Neural Network (decay) ----
ctrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
nn_grid <- expand.grid(size = 5, decay = c(0, 0.001, 0.005, 0.01, 0.02, 0.05, 0.1))
set.seed(123)

nn_tuned <- train(car_train[, -7], car_train[, 7], method = "nnet",
                  trControl = ctrl, tuneGrid = nn_grid, trace = FALSE)
plot(nn_tuned)


## ---- Tuning the Decision Tree (trials) ----
ctrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
tree_grid <- expand.grid(model = "tree",
                         trials = c(1, 5, 10, 15, 20, 25, 30, 40, 50, 60, 80, 100),
                         winnow = FALSE)
set.seed(123)

tree_tuned <- train(car_train[, -7], car_train[, 7], method = "C5.0",
                    trControl = ctrl, tuneGrid = tree_grid)
plot(tree_tuned)


## ============================================================
## PART 3: PRESENTATION OF SPECIFIED RESULTS
## ============================================================
## (This section will be finalised during the lab session once the
## specific figures to include are confirmed. Draft notes and
## interpretations for each required figure should be added here,
## along with Harvard-style references.)


## ============================================================
## REFERENCES (Harvard style - to be completed/expanded)
## ============================================================
## Kuhn, M. (2008) 'Building predictive models in R using the caret
##   package', Journal of Statistical Software, 28(5), pp. 1-26.
## Meyer, D. et al. (2023) e1071: Misc functions of the Department of
##   Statistics, Probability Theory Group. R package.
## Meyer, P. E. (2014) infotheo: Information-theoretic measures.
##   R package.
## UCI Machine Learning Repository (1997) Car Evaluation Data Set.
##   Available at: https://archive.ics.uci.edu/dataset/19/car+evaluation
##   (Accessed: [add date]).
## Wickham, H. (2016) ggplot2: Elegant Graphics for Data Analysis.
##   New York: Springer-Verlag.
