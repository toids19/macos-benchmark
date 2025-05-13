//MOLDOVANU_TUDOR_MAC_BENCHMARK//
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <stdint.h>
#include <pthread.h>
#include <sys/time.h>

//define memory size constants for ease of calculation
#define MB (1024 * 1024)
#define GB (1024 * MB)

//define the number of threads for parallel memory operations
#define NUM_THREADS 4

//structure to pass data to each thread (memory, size, type of test, offsets)
typedef struct {
    char *memory;
    size_t size;
    int write_test;        //flag to indicate if it's a write or read test
    size_t start_offset;   //starting position in memory for the thread
    size_t chunk_size;     //size of memory chunk for each thread
} thread_data_t;

//function to get the current time in seconds using high-resolution timers
double get_time_in_seconds() {
    struct timeval start;
    gettimeofday(&start, NULL); //get current time
    return start.tv_sec + (start.tv_usec / 1000000.0); //convert to seconds
}

//thread function for performing memory read or write operations
void *memory_test_thread(void *arg) {
    thread_data_t *data = (thread_data_t *)arg;
    volatile char temp;  //volatile variable to prevent optimization
    size_t end_offset = data->start_offset + data->chunk_size;

    //perform either write or read operations depending on the write_test flag
    for (size_t i = data->start_offset; i < end_offset; i++) {
        if (data->write_test) {
            data->memory[i] = 0x55; //write a pattern (0x55) to memory
        } else {
            temp = data->memory[i]; //read memory (use a volatile variable)
        }
    }
    return NULL;
}

int main() {
    //define the size of the memory to test (5 GB)
    size_t test_size = 5 * GB; //5 GB test size
    char *memory_block = (char *)malloc(test_size); //allocate memory

    //check if memory allocation succeeded
    if (memory_block == NULL) {
        printf("Memory allocation failed! Try reducing the memory size.\n");
        return 1; //exit with error code 1 if allocation fails
    }

    //declare thread-related variables
    pthread_t threads[NUM_THREADS]; //array to hold thread IDs
    thread_data_t thread_data[NUM_THREADS]; //array to hold thread data
    size_t chunk_size = test_size / NUM_THREADS; //size of memory chunk each thread will process

    //measure the memory write speed
    double start_time = get_time_in_seconds(); //get start time

    //create threads for writing memory
    for (int i = 0; i < NUM_THREADS; i++) {
        thread_data[i].memory = memory_block; //set memory block for each thread
        thread_data[i].size = test_size;     //set total size for each thread
        thread_data[i].write_test = 1;       //indicate write operation
        thread_data[i].start_offset = i * chunk_size; //start offset for each thread
        thread_data[i].chunk_size = chunk_size;       //set chunk size for each thread
        pthread_create(&threads[i], NULL, memory_test_thread, &thread_data[i]); //create thread
    }

    //join all threads (wait for them to finish)
    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    //measure the end time and calculate the write speed
    double end_time = get_time_in_seconds(); //get end time
    double write_time = end_time - start_time; //calculate time taken for writing
    double write_speed_gb_per_sec = (test_size / (double)GB) / write_time; //calculate speed in GB/s
    printf("Memory write speed: %.6f GB/s for %.2f GB\n", write_speed_gb_per_sec, (double)test_size / GB);

    //measure the memory read speed
    start_time = get_time_in_seconds(); //get start time for read test

    //create threads for reading memory
    for (int i = 0; i < NUM_THREADS; i++) {
        thread_data[i].write_test = 0; //set to read operation
        pthread_create(&threads[i], NULL, memory_test_thread, &thread_data[i]); //create thread for reading
    }

    //join all threads for reading
    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    //measure the end time and calculate the read speed
    end_time = get_time_in_seconds(); //get end time for read test
    double read_time = end_time - start_time; //calculate time taken for reading
    double read_speed_gb_per_sec = (test_size / (double)GB) / read_time; //calculate read speed in GB/s
    printf("Memory read speed: %.6f GB/s for %.2f GB\n", read_speed_gb_per_sec, (double)test_size / GB);

    //free the allocated memory
    free(memory_block);

    return 0; //return 0 to indicate successful execution
}
