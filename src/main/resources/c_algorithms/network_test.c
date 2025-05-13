//MOLDOVANU_TUDOR_MAC_BENCHMARK//
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <sys/time.h>

#define PORT 8080
#define BUFFER_SIZE 1024000  //1 MB buffer for testing network speed

//function to measure the difference in time between two timeval structures
long long timeval_diff(struct timeval *start, struct timeval *end) {
    return (end->tv_sec - start->tv_sec) * 1000000LL + (end->tv_usec - start->tv_usec);
}

int main() {
    int server_sock, client_sock;
    struct sockaddr_in server_addr, client_addr;
    pid_t pid;
    char buffer[BUFFER_SIZE] = {0};

    //create server socket
    server_sock = socket(AF_INET, SOCK_STREAM, 0);
    if (server_sock < 0) {
        perror("Failed to create server socket");
        exit(EXIT_FAILURE);
    }

    //initialize server address
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(PORT);

    //bind and listen on the server socket
    if (bind(server_sock, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Server bind failed");
        exit(EXIT_FAILURE);
    }
    if (listen(server_sock, 1) < 0) {
        perror("Listen failed");
        exit(EXIT_FAILURE);
    }

    pid = fork(); //create a new process
    if (pid == 0) { //child process - Client
        close(server_sock); //close the server socket in the client process

        //create client socket
        client_sock = socket(AF_INET, SOCK_STREAM, 0);
        if (client_sock < 0) {
            perror("Failed to create client socket");
            exit(EXIT_FAILURE);
        }

        //initialize client address to connect to the server
        memset(&client_addr, 0, sizeof(client_addr));
        client_addr.sin_family = AF_INET;
        client_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
        client_addr.sin_port = htons(PORT);

        sleep(1); //give server time to set up

        //connect client
        if (connect(client_sock, (struct sockaddr *)&client_addr, sizeof(client_addr)) < 0) {
            perror("Client connection failed");
            exit(EXIT_FAILURE);
        }

        //send data
        memset(buffer, 'A', BUFFER_SIZE);
        struct timeval start_time, end_time;
        gettimeofday(&start_time, NULL);
        send(client_sock, buffer, BUFFER_SIZE, 0);
        gettimeofday(&end_time, NULL);

        long long transfer_time = timeval_diff(&start_time, &end_time);
        printf("Transfer time: %lld microseconds\n", transfer_time);

        close(client_sock);
    } else { //parent process - Server
        //accept connection on server side
        struct sockaddr_in client;
        socklen_t client_len = sizeof(client);
        int accepted_sock = accept(server_sock, (struct sockaddr *)&client, &client_len);
        if (accepted_sock < 0) {
            perror("Accept failed");
            exit(EXIT_FAILURE);
        }

        //receive data
        recv(accepted_sock, buffer, BUFFER_SIZE, 0);

        close(accepted_sock);
        close(server_sock);
    }

    return 0;
}
