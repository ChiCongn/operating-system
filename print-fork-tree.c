
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdlib.h>
#include <sys/wait.h>

#define N 10 // Số lần tạo tiến trình con

// In khoảng trắng
void print_spaces(FILE *file, int depth) {
    for (int i = 0; i < depth; i++) {
        fprintf(file, "    ");
    }
}

// Tạo tiến trình con đệ quy
void create_processes(int depth, int iteration) {
    if (iteration == N) {
        return;
    }

    pid_t pid = fork();

    if (pid == 0) { // Nếu là tiến trình con
        FILE *file = fopen("print-processes-tree.txt", "a");
        if (file == NULL) {
            perror("Lỗi mở file");
            exit(1);
        }

        print_spaces(file, depth);
        fprintf(file, "    |-- PID = %d, Parent PID = %d\n", getpid(), getppid());
        fclose(file);

        create_processes(depth + 1, iteration + 1); // Gọi đệ quy để tạo tiến trình con tiếp theo

        exit(0);

    } else if (pid > 0) { // Nếu là tiến trình cha
        wait(NULL); // Chờ tiến trình con kết thúc
        create_processes(depth, iteration + 1); // Tiếp tục tạo tiến trình con

    } else {
        perror("Fork failed");
        return;
    }
}

int main() {
    FILE *file = fopen("print-processes-tree.txt", "w");
    if (file == NULL) {
        perror("Lỗi mở file");
        return 1;
    }

    fprintf(file, "Root Process: PID = %d\n", getpid());
    fclose(file);

    create_processes(0, 0);

    return 0;
}

