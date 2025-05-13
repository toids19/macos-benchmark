//MOLDOVANU_TUDOR_MAC_BENCHMARK//
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define SIZE 500  //adjustable size

//function to perform matrix multiplication and return execution time
double performMatrixMultiplication() {
    //dynamically allocate memory for matrices and check for allocation failure
    double **A = (double **)malloc(SIZE * sizeof(double *));
    double **B = (double **)malloc(SIZE * sizeof(double *));
    double **C = (double **)malloc(SIZE * sizeof(double *));

    if (A == NULL || B == NULL || C == NULL) {
        //allocation failed, return a specific error code
        return -1.0;
    }

    for (int i = 0; i < SIZE; i++) {
        A[i] = (double *)malloc(SIZE * sizeof(double));
        B[i] = (double *)malloc(SIZE * sizeof(double));
        C[i] = (double *)malloc(SIZE * sizeof(double));

        if (A[i] == NULL || B[i] == NULL || C[i] == NULL) {
            //allocation failed, return a specific error code
            return -1.0;
        }
    }

    srand((unsigned int)time(NULL));  //seed the random number generator

    //initialize matrices with random values
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            A[i][j] = (double)(rand() % 100);  //random values between 0 and 99
            B[i][j] = (double)(rand() % 100);  //random values between 0 and 99
        }
    }

    clock_t start = clock();  //start timing the matrix multiplication

    //matrix multiplication
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            C[i][j] = 0;  //initialize the result matrix element
            for (int k = 0; k < SIZE; k++) {
                C[i][j] += A[i][k] * B[k][j];  //multiply and accumulate
            }
        }
    }

    clock_t end = clock();  //end timing the matrix multiplication

    //free the allocated memory for matrices
    for (int i = 0; i < SIZE; i++) {
        free(A[i]);
        free(B[i]);
        free(C[i]);
    }
    free(A);
    free(B);
    free(C);

    //return the time in seconds
    return ((double)(end - start)) / CLOCKS_PER_SEC;
}

int main() {
    //call the matrix multiplication function and print the execution time
    double timeTaken = performMatrixMultiplication();

    if (timeTaken == -1.0) {
        printf("Memory allocation failed.\n");
    } else {
        printf("Matrix multiplication completed in %.6f seconds.\n", timeTaken);
    }

    return 0;
}
