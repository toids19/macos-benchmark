//MOLDOVANU_TUDOR_MAC_BENCHMARK//
#include <stdio.h>
#include <openssl/evp.h>
#include <string.h>
#include <time.h>
#define DATA_SIZE 8  //setting the size of the data to be encrypted/decrypted as 8 bytes

int main() {
    //128-bit key (16 bytes) for AES-128 encryption (using a hardcoded string)
    unsigned char key[16] = "thisisasecretkey";  // AES key (16 bytes = 128 bits)

    //initialization vector (IV) set to zero. In ECB mode, IV is not used but required by the API.
    unsigned char iv[16] = {0};  //zeroed-out IV, required by OpenSSL functions

    //data block to be encrypted (8 bytes of ASCII characters 'A' to 'H')
    unsigned char input[DATA_SIZE] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

    //buffers to hold the encrypted and decrypted data
    unsigned char encrypted[DATA_SIZE + 16];  //extra space for potential padding
    unsigned char decrypted[DATA_SIZE + 16];  //extra space for potential padding

    //create and initialize an EVP cipher context for encryption and decryption
    EVP_CIPHER_CTX *ctx = EVP_CIPHER_CTX_new();  //create a new EVP context
    if (!ctx) {
        fprintf(stderr, "Error creating EVP context.\n");  //error checking
        return 1;  //exit with error if context creation fails
    }

    //start measuring the total time for both encryption and decryption operations
    clock_t start = clock();  //capture start time
    // ----------- Encryption Process -----------
    //initialize the encryption operation with AES-128 in ECB mode
    if (EVP_EncryptInit_ex(ctx, EVP_aes_128_ecb(), NULL, key, iv) != 1) {
        fprintf(stderr, "Error initializing encryption.\n");  //error checking
        return 1;
    }

    //enable padding for the encryption process
    EVP_CIPHER_CTX_set_padding(ctx, 1);

    //perform encryption (input data size is DATA_SIZE)
    int len;//variable to store the length of the encrypted data
    if (EVP_EncryptUpdate(ctx, encrypted, &len, input, DATA_SIZE) != 1) {
        fprintf(stderr, "Error during encryption.\n");  //error checking
        return 1;
    }

    int ciphertext_len = len;  //store the length of the ciphertext

    //finalize the encryption (handle any padding if necessary)
    if (EVP_EncryptFinal_ex(ctx, encrypted + len, &len) != 1) {
        fprintf(stderr, "Error finalizing encryption.\n");  // Error checking
        return 1;
    }
    ciphertext_len += len;  //update the total length of the encrypted data

    // ----------- Decryption Process -----------
    //initialize the decryption operation with AES-128 in ECB mode
    if (EVP_DecryptInit_ex(ctx, EVP_aes_128_ecb(), NULL, key, iv) != 1) {
        fprintf(stderr, "Error initializing decryption.\n");  //error checking
        return 1;
    }

    //perform decryption (decrypt the encrypted data)
    if (EVP_DecryptUpdate(ctx, decrypted, &len, encrypted, ciphertext_len) != 1) {
        fprintf(stderr, "Error during decryption.\n");  //error checking
        return 1;
    }

    int plaintext_len = len;  //store the length of the decrypted data

    //finalize the decryption (handle any padding if necessary)
    if (EVP_DecryptFinal_ex(ctx, decrypted + len, &len) != 1) {
        fprintf(stderr, "Error finalizing decryption.\n");  //error checking
        return 1;
    }
    plaintext_len += len;  //update the total length of the decrypted data

    // ----------- Time Measurement -----------
    clock_t end = clock();  // Capture end time
    double total_time = ((double)(end - start)) / CLOCKS_PER_SEC;  //calculate elapsed time in seconds
    printf("Total encryption and decryption time: %f seconds.\n", total_time);  //output the time

    // ----------- Verification -----------
    //verify that the decrypted data matches the original input data
    if (memcmp(input, decrypted, DATA_SIZE) == 0) {  //compare original data with decrypted data
        printf("Decryption matches the original input.\n");
    } else {
        printf("Decryption does not match the original input.\n");  //in case of mismatch
    }

    //free the EVP context to release memory
    EVP_CIPHER_CTX_free(ctx);

    return 0;  //exit successfully
}
