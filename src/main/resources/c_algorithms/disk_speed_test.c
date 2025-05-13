//MOLDOVANU_TUDOR_MAC_BENCHMARK//
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

#define TEMP_DIR "/tmp/"
#define FILENAME TEMP_DIR "benchmark_test_file.bin"  //file in system's temp directory
#define BUFFER_SIZE 1048576  //1MB buffer size for read/write operations
#define FILE_SIZE (5LL * 1073741824) //total file size (5GB)

void write_benchmark() {
    //open the file for writing in binary mode
    FILE *file = fopen(FILENAME, "wb");
    if (!file) {  //if the file can't be opened, print an error message and return
        perror("Unable to open file for writing");
        return;
    }

    //allocate memory for the buffer used in the write operation
    char *buffer = (char *)malloc(BUFFER_SIZE);
    if (!buffer) {  //if memory allocation fails, print an error message and return
        perror("Memory allocation failed");
        fclose(file);
        return;
    }

    //fill the buffer with 'A' characters (this will be the data written to the file)
    memset(buffer, 'A', BUFFER_SIZE);

    //start measuring the time taken for the write operation
    clock_t start = clock();

    //write data to the file in chunks of BUFFER_SIZE (1MB) until the full file size is written
    for (long long written = 0; written < FILE_SIZE; written += BUFFER_SIZE) {
        fwrite(buffer, 1, BUFFER_SIZE, file);  //write BUFFER_SIZE bytes of data to the file
    }

    //end the time measurement after all data is written
    clock_t end = clock();

    //free the buffer memory and close the file after writing
    free(buffer);
    fclose(file);

    //calculate the time taken in seconds
    double time_taken = ((double)(end - start)) / CLOCKS_PER_SEC;

    //calculate the write speed in GB per second (FILE_SIZE is in bytes)
    double write_speed = FILE_SIZE / time_taken / 1024 / 1024 / 1024;  // Convert from bytes/second to GB/s

    //output the write speed
    printf("Write Speed: %.2f GB/s\n", write_speed);
}

void read_benchmark() {
    //open the file for reading in binary mode
    FILE *file = fopen(FILENAME, "rb");
    if (!file) {  //if the file can't be opened, print an error message and return
        perror("Unable to open file for reading");
        return;
    }

    //allocate memory for the buffer used in the read operation
    char *buffer = (char *)malloc(BUFFER_SIZE);
    if (!buffer) {  //if memory allocation fails, print an error message and return
        perror("Memory allocation failed");
        fclose(file);
        return;
    }

    //start measuring the time taken for the read operation
    clock_t start = clock();

    //read data from the file in chunks of BUFFER_SIZE (1MB) until the end of the file is reached
    size_t bytes_read;
    while ((bytes_read = fread(buffer, 1, BUFFER_SIZE, file)) > 0) {
        //in this benchmark, we just read the data but do nothing with it
    }

    //end the time measurement after all data is read
    clock_t end = clock();

    //free the buffer memory and close the file after reading
    free(buffer);
    fclose(file);

    //calculate the time taken in seconds
    double time_taken = ((double)(end - start)) / CLOCKS_PER_SEC;

    //calculate the read speed in GB per second (FILE_SIZE is in bytes)
    double read_speed = FILE_SIZE / time_taken / 1024 / 1024 / 1024;  //convert from bytes/second to GB/s

    //output the read speed
    printf("Read Speed: %.2f GB/s\n", read_speed);
}

int main() {
    printf("Starting SSD Benchmark...\n");  //print message indicating the start of the benchmark

    //perform write benchmark
    write_benchmark();

    //perform read benchmark
    read_benchmark();

    remove(FILENAME);  //delete the test file from the file system

    return 0;  //exit the program successfully
}
