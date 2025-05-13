//MOLDOVANU_TUDOR_MAC_BENCHMARK//
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

//function to launch an application and return the time it took to launch
double launch_application(const char* app_name) {
    struct timespec start, end;
    char command[256];

    //create the command to launch the application
    sprintf(command, "open -a '%s'", app_name);

    //start the timer and launch the application
    clock_gettime(CLOCK_MONOTONIC, &start);
    system(command);

    //end the timer
    clock_gettime(CLOCK_MONOTONIC, &end);

    //calculate the elapsed time in seconds
    double elapsed_time = (end.tv_sec - start.tv_sec);
    elapsed_time += (end.tv_nsec - start.tv_nsec) / 1000000000.0;
    return elapsed_time;
}

//function to close an application
void close_application(const char* app_name) {
    char command[256];

    //create the command to close the application
    sprintf(command, "osascript -e 'tell application \"%s\" to quit'", app_name);
    system(command);
}

int main() {
    //list of applications to launch
    const char* apps[] = {
            "Safari", "Notes", "Calculator", "Calendar", "Mail",
            "Messages", "Preview", "TextEdit", "Photos", "Dictionary"
    };
    int num_apps = sizeof(apps) / sizeof(apps[0]);
    double total_launch_time = 0.0;

    //launch each application and measure the launch time
    for (int i = 0; i < num_apps; i++) {
        double launch_time = launch_application(apps[i]);
        printf("%s launched in %.2f seconds\n", apps[i], launch_time);
        total_launch_time += launch_time;
    }

    //calculate and print the average launch time
    double average_launch_time = total_launch_time / num_apps;
    printf("Average launch time: %.2f seconds\n", average_launch_time);

    //close each application
    for (int i = 0; i < num_apps; i++) {
        close_application(apps[i]);
    }

    return 0;
}
