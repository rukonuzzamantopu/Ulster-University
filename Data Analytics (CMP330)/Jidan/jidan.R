# ---
# title: "CMP330 Data Analytics - Assessment 2 (Pre-Assignment)"
# author: "MD Ali Hasan Jidan (B01011849)"
# output:
#   html_notebook: default
#   pdf_document: default
# ---
# # Introduction
# This notebook contains my solution to the CMP330 Assessment 2 pre-assignment.
# The goal is to classify cars according to their acceptability
# (unacc,acc,good,vgood) based on six categorical features
# (buying,maint,doors,persons,lug_boot,safety) using several
# machine learning algorithms.
# ---
# # Part 1: Exploratory Data Analysis and Data Preparation
# ## 1.1 Loading the data
# The dataset is read in and every column is converted to a factor, since all
# variables (including the target) are categorical rather than numeric.
car_data <- read.csv("car.csv")
car_data <- data.frame(lapply(car_data, factor))

head(car_data)
str(car_data)

# ## 1.2 Basic frequency counts
# Before modelling, it's useful to see how the classes are distributed and how
# individual features relate to the target label.
# How many cars fall into each acceptability class?
table(car_data$acceptability)

# Cross-tabulation: buying price vs acceptability
table(car_data$buying, car_data$acceptability)

# Cross-tabulation: safety vs acceptability (an extra one I added, since 
# safety is intuitively a strong predictor of acceptability)
table(car_data$safety, car_data$acceptability)

summary(car_data)

# ## 1.3 Visualising the distributions
# I used `ggplot2` to visualise the target distribution on its own, and then
# broken down by other features to get a first impression of which variables
# might be useful predictors.
library(ggplot2)

# Fix the ordering of the acceptability levels so charts read logically
car_data$acceptability <- factor(car_data$acceptability,
                                  levels = c("unacc", "acc", "good", "vgood"))

# Overall distribution of the target class
ggplot(car_data, aes(x = acceptability)) +
  geom_bar(fill = "steelblue") +
  labs(title = "Distribution of Car Acceptability",
       x = "Acceptability", y = "Count")

# *Figure 1: Most cars in the dataset are rated unacc (693 out of 1000),
# while only a small number are good or vgood. This shows the classes are
# quite imbalanced, which is worth keeping in mind later when looking at
# model accuracy.*
# Acceptability broken down by buying price
ggplot(car_data, aes(x = acceptability, fill = buying)) +
  geom_bar(position = "dodge") +
  labs(title = "Acceptability by Buying Price",
       x = "Acceptability", y = "Count")

# *Figure 2: Cars with a high or vhigh buying price are almost never rated
# good or vgood - all of those ratings come from cars with low or med buying
# price. This suggests buying price on its own is a decent predictor of
# acceptability, though clearly not the only factor.*
# Extra plot: acceptability broken down by safety level
ggplot(car_data, aes(x = acceptability, fill = safety)) +
  geom_bar(position = "dodge") +
  labs(title = "Acceptability by Safety Level",
       x = "Acceptability", y = "Count")

# *Figure 3: Cars with low safety are never rated above unacc, and vgood
# only shows up for cars with high safety. This makes safety look like one
# of the strongest predictors of acceptability out of all the features,
# which matches what the mutual information scores show later on.*
# ## 1.4 Mutual information between variables
# Since none of the variables are numeric, Pearson correlation isn't
# appropriate. Instead, I used mutual information from the infotheo
# package, which measures how much knowing one categorical variable reduces
# uncertainty about another.
# install.packages("infotheo")
library(infotheo)

mi_matrix <- mutinformation(car_data)
mi_matrix

# Mutual information of every feature with the target (acceptability is 
# column 7)
mi_matrix[-7, 7]

# I also visualised this as a simple heatmap so the strongest relationships
# are easier to spot at a glance:
library(reshape2)

mi_melted <- melt(mi_matrix)
ggplot(mi_melted, aes(x = Var1, y = Var2, fill = value)) +
  geom_tile() +
  scale_fill_gradient(low = "white", high = "darkred") +
  labs(title = "Mutual Information Heatmap", x = "", y = "", fill = "MI") +
  theme(axis.text.x = element_text(angle = 45, hjust = 1))

# *Figure 4: The diagonal is dark simply because each feature has full
# mutual information with itself. Ignoring that, safety and persons have
# the strongest relationship with acceptability, followed by buying and
# maint, while doors and lug_boot barely matter. This is why safety,
# persons and lug_boot were chosen as the top 3 features for feature
# selection later, and it also matches the earlier bar charts where safety
# and buying price clearly affected the acceptability rating.*
# ## 1.5 Chi-squared tests of independence
# A chi-squared test checks whether two categorical variables are statistically
# independent.
chisq.test(car_data$buying, car_data$acceptability)
chisq.test(car_data$buying, car_data$maint)

# Extra test: is safety independent of acceptability?
chisq.test(car_data$safety, car_data$acceptability)

# ## 1.6 Train/test split
# As specified in the assignment brief, the data is split 80/20 using a fixed
# seed so that results are reproducible and comparable across students.
# **This exact code must be used as given.**
library(caTools)
set.seed(123)
split <- sample.split(car_data$acceptability, SplitRatio = 0.8)
car_train <- subset(car_data, split == TRUE)
car_test  <- subset(car_data, split == FALSE)

summary(car_train$acceptability)
summary(car_test$acceptability)
prop.table(table(car_train$acceptability))
prop.table(table(car_test$acceptability))

# ---
# # Part 2a: Application of Machine Learning Algorithms
# Each of the five required algorithms is trained on car_train and evaluated
# on car_test using a confusion matrix (accuracy, kappa, per-class
# sensitivity/specificity, etc.).
# ### Decision Tree (C5.0)
library(caret)
library(C50)

tree_model <- C5.0(car_train[-7], car_train$acceptability)
tree_pred  <- predict(tree_model, car_test)
confusionMatrix(tree_pred, car_test$acceptability)

# *The basic decision tree gets 96.5% accuracy and a kappa of 0.925 on the
# test set, correctly classifying unacc and vgood almost perfectly. The good
# class has slightly lower precision, likely due to its small sample size,
# but overall the model fits the data well even before tuning.*
# ### Naive Bayes
library(e1071)
set.seed(123)

nb_model <- naiveBayes(car_train[, -7], car_train$acceptability)
nb_pred  <- predict(nb_model, car_test)
confusionMatrix(nb_pred, car_test$acceptability)

# *Naive Bayes gets 85% accuracy with a kappa of 0.664, noticeably lower
# than the decision tree. It struggles most with vgood, catching only 2 out
# of 7 cases (sensitivity 0.286), likely because Naive Bayes assumes
# features are independent, which doesn't fully hold in this dataset.*
# ### Multinomial Logistic Regression
library(nnet)

logreg_model <- multinom(acceptability ~ ., data = car_train)
logreg_pred  <- predict(logreg_model, car_test)
confusionMatrix(logreg_pred, car_test$acceptability)

# *Multinomial logistic regression gets 93.5% accuracy and a kappa of 0.864,
# sitting between Naive Bayes and the decision tree. It handles all four
# classes reasonably well, including vgood (sensitivity 0.857), which is a
# clear improvement over Naive Bayes.*
# ### Support Vector Machine
library(kernlab)

svm_model_ksvm <- ksvm(acceptability ~ ., data = car_train, kernel = "vanilladot")
svm_pred_ksvm  <- predict(svm_model_ksvm, car_test)
confusionMatrix(svm_pred_ksvm, car_test$acceptability)

# *The linear-kernel SVM gets 93.5% accuracy and a kappa of 0.859, very
# close to logistic regression. It classifies vgood perfectly and does well
# on good too, though it misses a few unacc and acc cases.*
# Alternative SVM implementation using e1071, for comparison
svm_model_e1071 <- svm(acceptability ~ ., data = car_train)
svm_pred_e1071  <- predict(svm_model_e1071, car_test)
confusionMatrix(svm_pred_e1071, car_test$acceptability)

# *The e1071 SVM gets 88% accuracy but a lower kappa of 0.744, since it
# never predicts good or vgood at all - every good/vgood car in the test
# set gets misclassified as acc. This makes it clearly weaker than the
# ksvm version above, even though the overall accuracy looks decent.*
# ### Neural Network
nn_pred <- predict(nn_model, car_test, type = "class")
nn_pred <- factor(nn_pred, levels = levels(car_test$acceptability))
confusionMatrix(nn_pred, car_test$acceptability)

# *The basic neural network (5 hidden units) gets 95.5% accuracy and a kappa
# of 0.899, doing well across all four classes including a perfect score on
# unacc. This is one of the strongest basic models so far, close to the
# decision tree.*
# ## Using caret::train() with cross-validation
# For each algorithm, 10-fold cross-validation is used to tune hyperparameters
# automatically via the train() function from caret.
# ### Decision Tree with CV
ctrl <- trainControl(method = "cv", number = 10)
set.seed(123)

tree_cv_model <- train(car_train[, -7], car_train[, 7], method = "C5.0", trControl = ctrl)
tree_cv_model
tree_cv_pred <- predict(tree_cv_model, car_test)
confusionMatrix(tree_cv_pred, car_test$acceptability)

# *After tuning with 10-fold cross-validation, the decision tree improves
# from 96.5% to 99% accuracy, with kappa jumping to 0.978. Cross-validation
# picked trials = 20 and model = rules as the best settings, and the tuned
# model now gets every class almost perfectly right, showing that hyperparameter
# tuning clearly helps here.*
# ### Naive Bayes with CV
ctrl <- trainControl(method = "cv", number = 10)
set.seed(123)

nb_cv_model <- train(car_train[, -7], car_train[, 7], method = "nb", trControl = ctrl)
nb_cv_model
nb_cv_pred <- predict(nb_cv_model, car_test)
confusionMatrix(nb_cv_pred, car_test$acceptability)

# *After tuning with cross-validation, Naive Bayes still gets 85% accuracy
# and a kappa of 0.664 - basically no change from the basic version. This
# makes sense since there's only one real parameter to tune (fL), and cross-
# validation picked fL = 0, the same as the default, so the model stayed
# the same.*
# ### Naive Bayes with CV (tuned fL grid)
nb_grid <- expand.grid(fL = c(0, 0.5, 1, 2, 5), 
                        usekernel = TRUE, 
                        adjust = 1)

nb_cv_model2 <- train(car_train[, -7], car_train[, 7], 
                       method = "nb", 
                       trControl = ctrl,
                       tuneGrid = nb_grid)
nb_cv_model2

# *Testing different fL values (0, 0.5, 1, 2, 5) shows accuracy actually
# gets slightly worse as fL increases, from 83.6% at fL=0 down to 81.5%
# at fL=5. Cross-validation correctly picks fL=0 as the best setting,
# confirming that Laplace smoothing does not help on this dataset.*
# ### Logistic Regression with CV
ctrl <- trainControl(method = "cv", number = 10)
set.seed(123)

logreg_cv_model <- train(car_train[, -7], car_train[, 7], method = "multinom",
                          trControl = ctrl, trace = FALSE)
logreg_cv_model
logreg_cv_pred <- predict(logreg_cv_model, car_test)
confusionMatrix(logreg_cv_pred, car_test$acceptability)

# *After cross-validation, logistic regression stays roughly the same at
# 93% accuracy and a kappa of 0.853, barely different from the basic
# version (93.5%). CV picked a very small decay (1e-04), and larger decay
# values (0.1) actually hurt accuracy slightly, suggesting the basic model
# was already close to optimal.*
# ### Neural Network with CV
ctrl <- trainControl(method = "cv", number = 10)
set.seed(123)

nn_cv_model <- train(car_train[, -7], car_train[, 7], method = "nnet",
                      trControl = ctrl, trace = FALSE)
nn_cv_model
nn_cv_pred <- predict(nn_cv_model, car_test)
confusionMatrix(nn_cv_pred, car_test$acceptability)

# *After tuning size and decay together, the neural network improves from
# 95.5% to 98% accuracy and kappa 0.956. CV picked the largest network
# tested (size=5, decay=0.1), and the results clearly show bigger size
# consistently helps - showing hyperparameter tuning had a real,
# measurable impact here.*
# ### SVM with CV
set.seed(123)
svm_cv_model <- ksvm(acceptability ~ ., data = car_train, kernel = "vanilladot", cross = 10)
svm_cv_model
svm_cv_pred <- predict(svm_cv_model, car_test)
confusionMatrix(svm_cv_pred, car_test$acceptability)

# *The 10-fold cross-validated SVM gets 93.5% accuracy and a kappa of 0.859,
# identical to the basic version, with a low cross-validation error of
# 0.0975. Since the linear kernel has no real hyperparameters to tune here,
# this confirms the model was already stable rather than overfitting to a
# lucky split.*
# ## Feature selection using mutual information
# The mutual information between each feature and the target (computed on the
# training set only, to avoid data leakage) is used to rank features. The
# top three — persons,lug_boot,safety — are then used to retrain
# simplified models.
mi_train <- mutinformation(car_train)
mi_train
mi_train[7, -7]

# ### Trying a range of feature-set sizes (x)
# Rather than fixing x = 3, the number of top features is varied across
# x = 2, 3, 4, 5 (ranked by mutual information with the target) and the
# Decision Tree (C5.0) is retrained each time, so the effect of feature-set
# size on accuracy can be compared directly.
mi_ranked <- sort(mi_train[7, -7], decreasing = TRUE)
mi_ranked

x_values <- 2:5
fs_accuracy <- data.frame(x = x_values, Accuracy = NA)

for (i in seq_along(x_values)) {
  x <- x_values[i]
  top_features <- names(mi_ranked)[1:x]

  tree_x_model <- C5.0(car_train[, top_features], car_train$acceptability)
  tree_x_pred  <- predict(tree_x_model, car_test)
  acc <- confusionMatrix(tree_x_pred, car_test$acceptability)$overall["Accuracy"]

  fs_accuracy$Accuracy[i] <- acc
  cat("x =", x, "- features:", paste(top_features, collapse = ", "),
      "- Accuracy:", round(acc, 4), "\n")
}

fs_accuracy

ggplot(fs_accuracy, aes(x = x, y = Accuracy)) +
  geom_line(color = "steelblue") +
  geom_point(size = 2, color = "steelblue") +
  scale_x_continuous(breaks = x_values) +
  labs(title = "Decision Tree accuracy vs. number of top features (x)",
       x = "Number of top features (x)", y = "Test set accuracy")

# *Accuracy clearly increases as more top features are used, from about 80%
# with just 2 features to over 96% with 5. This shows the top-ranked
# features (persons and safety) are useful but not enough on their own -
# combining all of them together gives the best result, so using just x=3
# features is a bit of a trade-off rather than the optimal choice.*
# ### Decision Tree with selected features
tree_fs_model <- C5.0(car_train[, 4:6], car_train$acceptability)
tree_fs_pred  <- predict(tree_fs_model, car_test)
confusionMatrix(tree_fs_pred, car_test$acceptability)

# *Using the fixed top-3 features (persons, lug_boot, safety) directly, the
# decision tree gets 81.5% accuracy and kappa 0.603 - close to what the x=3
# point showed in the graph above. Notably, this model never predicts good
# or vgood at all, showing that dropping buying, maint and doors loses
# information needed to catch the smaller classes, even though safety and
# persons alone still get most unacc/acc cases right.*
# ### Naive Bayes with selected features
nb_fs_model <- naiveBayes(car_train[, 4:6], car_train$acceptability)
nb_fs_pred  <- predict(nb_fs_model, car_test)
confusionMatrix(nb_fs_pred, car_test$acceptability)

# *With only the top-3 features, Naive Bayes drops to 78.5% accuracy and
# kappa 0.495, its weakest result so far. Like the decision tree above, it
# completely misses good and vgood, confirming that these smaller classes
# need the extra features that were dropped.*
# ### Logistic Regression with selected features
logreg_fs_model <- multinom(acceptability ~ persons + lug_boot + safety, data = car_train)
logreg_fs_pred  <- predict(logreg_fs_model, car_test)
confusionMatrix(logreg_fs_pred, car_test$acceptability)

# *Logistic regression with just the top-3 features gets 81.5% accuracy
# and kappa 0.603 - the same result as the decision tree with selected
# features above. It also fails to predict good or vgood at all, again
# showing that these three features alone are not enough to identify the
# rarer classes.*
# ---
# # Part 2b: Improving Model Performance via Hyperparameter Tuning
# ## Tuning the Neural Network (`decay`)
ctrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
nn_grid <- expand.grid(size = 5, decay = c(0, 0.001, 0.005, 0.01, 0.02, 0.05, 0.1))
set.seed(123)

nn_tuned <- train(car_train[, -7], car_train[, 7], method = "nnet",
                   trControl = ctrl, tuneGrid = nn_grid, trace = FALSE)
plot(nn_tuned)

# *Accuracy jumps sharply from about 94% at decay=0 to over 96% by decay=0.01,
# then flattens out and even dips slightly after decay=0.05. This shows a
# small amount of weight decay clearly helps the network generalise better,
# but too much regularisation starts to hurt performance again.*
# ## Tuning the Decision Tree (`trials`)
ctrl <- trainControl(method = "repeatedcv", number = 10, repeats = 10)
tree_grid <- expand.grid(model = "tree",
                          trials = c(1, 5, 10, 15, 20, 25, 30, 40, 50, 60, 80, 100),
                          winnow = FALSE)
set.seed(123)

tree_tuned <- train(car_train[, -7], car_train[, 7], method = "C5.0",
                     trControl = ctrl, tuneGrid = tree_grid)
plot(tree_tuned)

# *Accuracy improves quickly as trials increase from 1 to around 20-30,
# then levels off and stays roughly flat all the way to 100. This shows
# boosting helps up to a point, but adding more trials beyond about 30
# gives almost no extra benefit while making the model more complex for
# no real gain.*
# ---
# # Part 3. Presentation of Specified Results
# *(This section will be finalised during the lab session once the specific
# figures to include are confirmed. Draft notes and interpretations for each
# required figure should be added here, along with Harvard-style references.)*
# ## References (Harvard style)
# - Kuhn, M. (2008) 'Building predictive models in R using the caret package',
#   *Journal of Statistical Software*, 28(5), pp. 1-26.
# - Meyer, D. et al. (2023) *e1071: Misc functions of the Department of
#   Statistics, Probability Theory Group*. R package.
# - Meyer, P. E. (2014) *infotheo: Information-theoretic measures*. R package.
# - UCI Machine Learning Repository (1997) *Car Evaluation Data Set*. Available
#   at: https://archive.ics.uci.edu/dataset/19/car+evaluation (Accessed: 17 August 2026).
# - Wickham, H. (2016) *ggplot2: Elegant Graphics for Data Analysis*. New York:
#   Springer-Verlag.
# - Ripley, B. (2023) *nnet: Feed-Forward Neural Networks and Multinomial
#   Log-Linear Models*. R package.
# - Kuhn, M. (2023) *C50: C5.0 Decision Trees and Rule-Based Models*. R package.
# - Karatzoglou, A. et al. (2004) *kernlab: An S4 Package for Kernel Methods
#   in R*. R package.
# - Wickham, H. (2007) *reshape2: Flexibly Reshape Data*. R package.