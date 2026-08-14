## ============================================================
## CMP330 Assessment 2
## Author: Topu
## ============================================================
## This code provides solutions for the pre-assignment and follow-up
## lab test.
##
## First, read in the file and convert everything to factors. This
## code will be shared with students in the specification for the
## pre-assignment that is released to students.
## ============================================================

df <- read.csv("car.csv")
df <- data.frame(lapply(df, factor))
head(df)
str(df)


## ============================================================
## 1. EXPLORATORY DATA ANALYSIS AND DATA PREPARATION
## ============================================================

## ---- General information about target and other variables ----
table(df$acceptability)
table(df$buying, df$acceptability)
summary(df)


## ---- Plot distributions of variables ----
library(ggplot2)
df$acceptability <- factor(df$acceptability, levels = c("unacc", "acc", "good", "vgood"))
ggplot(df, aes(x = acceptability)) + geom_bar()
ggplot(df, aes(x = acceptability, fill = buying)) + geom_bar()


## ---- Mutual information ----
# install.packages("infotheo")
library("infotheo")
m <- mutinformation(df)
m

# Could use following code for each row, replacing [-1,1] with [-2,2], etc.
max(m[-1, 1])


## ---- Chi-squared tests ----
chisq.test(df$buying, df$acceptability)
chisq.test(df$buying, df$maint)


## ---- Split data into training and test sets ----
library(caTools)
set.seed(123)
split <- sample.split(df$acceptability, SplitRatio = 0.8)
df_train <- subset(df, split == TRUE)
df_test  <- subset(df, split == FALSE)

summary(df_train$acceptability)
summary(df_test$acceptability)
prop.table(table(df_train$acceptability))
prop.table(table(df_test$acceptability))


## ============================================================
## 2a. DATA ANALYTICS SOLUTION: APPLICATION OF MACHINE LEARNING
## ============================================================

## ---- Decision Tree, C5.0 ----
library(caret)
library(C50)
dt_model <- C5.0(df_train[-7], df_train$acceptability)
dt_pred  <- predict(dt_model, df_test)
confusionMatrix(dt_pred, df_test$acceptability)


## ---- Naive Bayes ----
library(e1071)
set.seed(123)
nb_model <- naiveBayes(df_train[, -7], df_train$acceptability)
nb_pred  <- predict(nb_model, df_test)
confusionMatrix(nb_pred, df_test$acceptability)


## ---- Logistic Regression ----
library(nnet)
lg_model <- multinom(acceptability ~ ., data = df_train)
lg_pred  <- predict(lg_model, df_test)
confusionMatrix(lg_pred, df_test$acceptability)


## ---- SVM ----
library(kernlab)
svm_model <- ksvm(acceptability ~ ., data = df_train, kernel = "vanilladot")
svm_pred  <- predict(svm_model, df_test)
confusionMatrix(svm_pred, df_test$acceptability)

library(e1071)
svm_model <- svm(acceptability ~ ., data = df_train)
svm_pred  <- predict(svm_model, df_test)
confusionMatrix(svm_pred, df_test$acceptability)


## ---- Neural Network ----
# see https://www.geeksforgeeks.org/r-language/neural-networks-using-the-r-nnet-package/
nn_model <- nnet(acceptability ~ ., data = df_train, size = 5)
nn_pred  <- predict(nn_model, df_test, type = "class")
confusionMatrix(factor(nn_pred), df_test$acceptability)


## ---- Decision Tree with CV ----
ctrl <- trainControl(method = 'cv', number = 10)
set.seed(123)
dt_cv_model <- train(df_train[, -7], df_train[, 7], 'C5.0', trControl = ctrl)
dt_cv_model
dt_cv_pred <- predict(dt_cv_model, df_test)
confusionMatrix(dt_cv_pred, df_test$acceptability)


## ---- Naive Bayes with CV ----
ctrl <- trainControl(method = 'cv', number = 10)
set.seed(123)
nb_cv_model <- train(df_train[, -7], df_train[, 7], 'nb', trControl = ctrl)
nb_cv_model
nb_cv_pred <- predict(nb_cv_model, df_test)
confusionMatrix(nb_cv_pred, df_test$acceptability)


## ---- Logistic Regression with CV ----
ctrl <- trainControl(method = 'cv', number = 10)
set.seed(123)
lg_cv_model <- train(df_train[, -7], df_train[, 7], 'multinom', trControl = ctrl, trace = FALSE)
lg_cv_model
lg_cv_pred <- predict(lg_cv_model, df_test)
confusionMatrix(lg_cv_pred, df_test$acceptability)


## ---- Neural Network with CV ----
ctrl <- trainControl(method = 'cv', number = 10)
set.seed(123)
nn_cv_model <- train(df_train[, -7], df_train[, 7], 'nnet', trControl = ctrl, trace = FALSE)
nn_cv_model
nn_cv_pred <- predict(nn_cv_model, df_test)
confusionMatrix(nn_cv_pred, df_test$acceptability)


## ---- SVM with CV ----
ctrl <- trainControl(method = 'cv', number = 10)
set.seed(123)
svm_cv_model <- ksvm(acceptability ~ ., data = df_train, kernel = "vanilladot", cross = 10)
svm_cv_model
svm_cv_pred <- predict(svm_cv_model, df_test)
confusionMatrix(svm_cv_pred, df_test$acceptability)


## ---- Feature Selection ----
m <- mutinformation(df_train)
m
m[7, -7]


## ---- Decision Tree with Feature Selection ----
dt_fs_model <- C5.0(df_train[, 4:6], df_train$acceptability)
dt_fs_pred  <- predict(dt_fs_model, df_test)
confusionMatrix(dt_fs_pred, df_test$acceptability)


## ---- Naive Bayes with Feature Selection ----
nb_fs_model <- naiveBayes(df_train[, 4:6], df_train$acceptability)
nb_fs_pred  <- predict(nb_fs_model, df_test)
confusionMatrix(nb_fs_pred, df_test$acceptability)


## ---- Logistic Regression with Feature Selection ----
lg_fs_model <- multinom(acceptability ~ persons + lug_boot + safety, data = df_train)
lg_fs_pred  <- predict(lg_fs_model, df_test)
confusionMatrix(lg_fs_pred, df_test$acceptability)


## ============================================================
## 2b. DATA ANALYTICS SOLUTION: IMPROVING MODEL PERFORMANCE
## ============================================================

## ---- Tuning for Neural Network ----
ctrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
grid <- expand.grid(size = 5, decay = c(0, 0.001, 0.005, 0.01, 0.02, 0.05, 0.1))
set.seed(123)
m <- train(df_train[, -7], df_train[, 7], method = "nnet", trControl = ctrl,
           tuneGrid = grid, trace = FALSE)
plot(m)


## ---- Tuning for DT ----
ctrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
grid <- expand.grid(model = "tree", trials = c(1, 5, 10, 15, 20, 25, 30, 40, 50, 60, 80, 100),
                     winnow = FALSE)
set.seed(123)
m <- train(df_train[, -7], df_train[, 7], method = "C5.0", trControl = ctrl, tuneGrid = grid)
plot(m)
