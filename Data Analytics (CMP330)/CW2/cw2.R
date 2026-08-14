## ============================================================
## R Notebook - CMP330 Car Acceptability Analysis
## ============================================================
## Converted from R Markdown (.Rmd) to a plain R script.
## Interpretive notes originally written as prose between chunks
## have been kept as comments directly above/below the relevant code.
## ============================================================

df <- read.csv("car.csv")
df <- data.frame(lapply(df, factor))
head(df)
str(df)
table(df$acceptability)
table(df$buying, df$acceptability)


## ---- Bar chart: acceptability distribution ----
library(ggplot2)
ggplot(df, aes(x = acceptability)) + geom_bar()


## ---- Bar chart: acceptability by safety ----
ggplot(df, aes(x = acceptability, fill = safety)) + geom_bar()

# The bar chart shows that all "vgood" rated cars have high safety,
# suggesting high safety is required (but not sufficient) for a
# car to be rated very good.


## ---- Mutual information ----
# install.packages("infotheo")
library(infotheo)
m <- mutinformation(df)
m

# Safety and persons show the strongest mutual information with
# acceptability, while doors shows almost none.


## ---- Chi-squared tests ----
chisq.test(df$buying, df$acceptability)
chisq.test(df$buying, df$maint)

# Buying price is significantly associated with acceptability (p < 0.001),
# but not with maintenance cost (p = 0.80), showing the test correctly
# distinguishes related and unrelated variable pairs.


## ---- Train/test split ----
# install.packages("caTools")
library(caTools)
set.seed(123)
split <- sample.split(df$acceptability, SplitRatio = 0.8)
df_train <- subset(df, split == TRUE)
df_test  <- subset(df, split == FALSE)


## ============================================================
## APPLICATION OF MACHINE LEARNING ALGORITHMS
## ============================================================

## ---- Decision Tree (C5.0) ----
# install.packages("C50")
# install.packages("caret")
library(C50)
library(caret)

dt_model <- C5.0(df_train[-7], df_train$acceptability)
dt_pred  <- predict(dt_model, df_test)
confusionMatrix(dt_pred, df_test$acceptability)


## ---- Naive Bayes ----
# install.packages("e1071")
library(e1071)

set.seed(123)
nb_model <- naiveBayes(df_train[, -7], df_train$acceptability)
nb_pred  <- predict(nb_model, df_test)
confusionMatrix(nb_pred, df_test$acceptability)

# Naive Bayes achieved 85% accuracy, notably lower than the decision
# tree's 96.5%. This may be because naive Bayes assumes feature
# independence, while acceptability appears to depend on combinations
# of features (e.g. safety and persons together).


## ---- Multinomial Logistic Regression ----
library(nnet)

lg_model <- multinom(acceptability ~ ., data = df_train)
lg_pred  <- predict(lg_model, df_test)
confusionMatrix(lg_pred, df_test$acceptability)

# Multinomial logistic regression achieved 93.5% accuracy, second-best
# so far behind the decision tree.


## ---- SVM ----
library(e1071)
svm_model <- svm(acceptability ~ ., data = df_train)
svm_pred  <- predict(svm_model, df_test)
confusionMatrix(svm_pred, df_test$acceptability)

# SVM achieved 88% accuracy but completely failed to predict "good" or
# "vgood" classes, always defaulting to "acc" or "unacc" -- likely due
# to class imbalance in the training data.


## ---- Neural Network ----
nn_model <- nnet(acceptability ~ ., data = df_train, size = 5)
nn_pred  <- predict(nn_model, df_test, type = "class")
confusionMatrix(factor(nn_pred), df_test$acceptability)

# Neural network achieved ~92% accuracy, performing reasonably well
# across all classes, though slightly below the decision tree.


## ---- Decision Tree with CV ----
library(caret)
ctrl <- trainControl(method = 'cv', number = 10)
set.seed(123)
dt_cv_model <- train(df_train[, -7], df_train[, 7], 'C5.0', trControl = ctrl)
dt_cv_model

dt_cv_pred <- predict(dt_cv_model, df_test)
confusionMatrix(dt_cv_pred, df_test$acceptability)

# Applying 10-fold cross-validation with caret improved the decision
# tree's accuracy from 96.5% to 99%, correctly classifying every
# "unacc" and "vgood" case in the test set.


## ---- Naive Bayes with CV ----
ctrl <- trainControl(method = 'cv', number = 10)
set.seed(123)
nb_cv_model <- train(df_train[, -7], df_train[, 7], 'nb', trControl = ctrl)
nb_cv_model
nb_cv_pred <- predict(nb_cv_model, df_test)
confusionMatrix(nb_cv_pred, df_test$acceptability)

# Cross-validation did not improve naive Bayes (still 85% accuracy) --
# unlike the decision tree, which improved from 96.5% to 99%. This
# suggests naive Bayes' independence assumption is a fundamental
# limitation on this dataset, not something tuning can fix.


## ---- Logistic Regression with CV ----
ctrl <- trainControl(method = 'cv', number = 10)
set.seed(123)
lg_cv_model <- train(df_train[, -7], df_train[, 7], 'multinom', trControl = ctrl, trace = FALSE)
lg_cv_model
lg_cv_pred <- predict(lg_cv_model, df_test)
confusionMatrix(lg_cv_pred, df_test$acceptability)

# Cross-validation confirmed decay=0 as optimal, matching the basic
# model's default -- accuracy stayed at 93.5%, unchanged from the
# non-CV version.


## ---- SVM with CV ----
set.seed(123)
# install.packages("kernlab")
library(kernlab)
svm_cv_model <- ksvm(acceptability ~ ., data = df_train, kernel = "vanilladot", cross = 10)
svm_cv_model
svm_cv_pred <- predict(svm_cv_model, df_test)
confusionMatrix(svm_cv_pred, df_test$acceptability)

# SVM improved dramatically with cross-validation (88% -> 94.5%), and
# critically, it stopped ignoring the rare "good"/"vgood" classes
# entirely -- correctly identifying 6/8 good and 7/7 vgood cars,
# compared to 0 in the basic version.


## ---- Neural Network with CV ----
ctrl <- trainControl(method = 'cv', number = 10)
set.seed(123)
nn_cv_model <- train(df_train[, -7], df_train[, 7], 'nnet', trControl = ctrl, trace = FALSE)
nn_cv_model
nn_cv_pred <- predict(nn_cv_model, df_test)
confusionMatrix(nn_cv_pred, df_test$acceptability)

# Neural network improved from ~92% to 97.5% with cross-validation,
# which found size=5, decay=0.1 as optimal -- showing that a moderate
# overfitting penalty (decay) combined with sufficient complexity
# (size) outperforms either alone.


## ---- Feature selection ----
colnames(df_train)

## Decision Tree with selected features (safety, persons, buying)
dt_fs_model <- C5.0(df_train[, c("safety", "persons", "buying")], df_train$acceptability)
dt_fs_pred  <- predict(dt_fs_model, df_test)
confusionMatrix(dt_fs_pred, df_test$acceptability)

## Naive Bayes with selected features
nb_fs_model <- naiveBayes(df_train[, c("safety", "persons", "buying")], df_train$acceptability)
nb_fs_pred  <- predict(nb_fs_model, df_test)
confusionMatrix(nb_fs_pred, df_test$acceptability)

## Logistic Regression with selected features
lg_fs_model <- multinom(acceptability ~ safety + persons + buying, data = df_train)
lg_fs_pred  <- predict(lg_fs_model, df_test)
confusionMatrix(lg_fs_pred, df_test$acceptability)

# NOTE: the original notebook repeated the logistic regression feature-
# selection chunk twice (identical code run twice in a row). Only kept
# once here -- remove this comment and re-add the duplicate chunk if
# you specifically need to show it running twice.


## ============================================================
## IMPROVING MODEL PERFORMANCE VIA HYPERPARAMETER TUNING
## ============================================================

## ---- Tuning the Neural Network (decay) ----
ctrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
grid <- expand.grid(size = 5, decay = c(0, 0.001, 0.005, 0.01, 0.02, 0.05, 0.1))
set.seed(123)
m <- train(df_train[, -7], df_train[, 7], method = "nnet", trControl = ctrl,
           tuneGrid = grid, trace = FALSE)
plot(m)

# Tuning weight decay for the neural network revealed a clear pattern:
# accuracy jumped sharply from ~93.8% at decay=0 to ~96% with even a
# small decay penalty, peaking around decay=0.02 (~96.3%), then
# gradually declining as decay increased further to 0.1. This confirms
# a moderate regularization penalty improves generalization, while
# excessive penalty slightly underfits the model.


## ---- Tuning the Decision Tree (trials) ----
ctrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
grid <- expand.grid(model = "tree", trials = c(1, 5, 10, 15, 20, 25, 30, 40, 50, 60, 80, 100),
                     winnow = FALSE)
set.seed(123)
m <- train(df_train[, -7], df_train[, 7], method = "C5.0", trControl = ctrl, tuneGrid = grid)
plot(m)

# Tuning C5.0's boosting trials showed accuracy rising sharply from
# ~94.3% (trials=1) to ~96% (trials~20), peaking around trials~30-40
# (~96.2%), then plateauing with no meaningful further gain up to
# trials=100. This suggests trials~30 offers the best accuracy-to-
# computation trade-off.
