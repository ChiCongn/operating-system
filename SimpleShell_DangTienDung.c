#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/wait.h>

/*
 * Filename: SimpleShell.c
 * Original Author: Unknown
 * Modified by: Đặng Tiến Dũng
 * Source: Unknown (copied from an external source)
 * Description: A simple shell implementation in C.
 * License: MIT License (or follow the original author's license if known)
 *
 * Compilation:
 *   gcc SimpleShell.c -o SimpleShell
 *
 * Usage:
 *   ./SimpleShell
 *   Example commands:
 *     myshell> ls
 *     myshell> pwd
 *     myshell> echo "Hello, world!"
 *     myshell> exit
 *
 * Notes:
 *   - Supports basic shell commands.
 *   - Does not support advanced features like pipes (|) or background execution (&).
 */


#define MAX_LINE 80
#define MAX_COMMANDS (MAX_LINE / 2 + 1)

// Hàm tách chuỗi lệnh thành các tham số
void tokenize(char* buffer, char* args[MAX_COMMANDS], int* background) {
    int count = 0;
    char* token = strtok(buffer, " \n");
    while (token) {
        if (strcmp(token, "&") == 0) {
            *background = 1; // Nếu có '&', đánh dấu chạy nền
            break;
        }
        args[count++] = token;
        token = strtok(NULL, " \n");
    }
    args[count] = NULL;
}

// Hàm tìm toán tử đặc biệt ('|' hoặc '>' hoặc '<')
int find_operator(char** args, const char* op) {
    for (int i = 0; args[i]; i++) {
        if (strcmp(args[i], op) == 0) return i;
    }
    return -1;
}

// Hàm thực thi lệnh
void execute_command(char** args) {
    int pipe_pos = find_operator(args, "|");
    int output_redirect_pos = find_operator(args, ">");
    int input_redirect_pos = find_operator(args, "<");

    if (pipe_pos != -1) { // Xử lý pipe
        args[pipe_pos] = NULL;
        int pipe_fd[2];
        pipe(pipe_fd);
        if (fork() == 0) {
            dup2(pipe_fd[1], STDOUT_FILENO);
            close(pipe_fd[0]); close(pipe_fd[1]);
            execvp(args[0], args);
            perror("execvp failed"); exit(1);
        }
        if (fork() == 0) {
            dup2(pipe_fd[0], STDIN_FILENO);
            close(pipe_fd[0]); close(pipe_fd[1]);
            execvp(args[pipe_pos + 1], args + pipe_pos + 1);
            perror("execvp failed"); exit(1);
        }
        close(pipe_fd[0]); close(pipe_fd[1]);
        wait(NULL); wait(NULL);
    } else if (output_redirect_pos != -1 || input_redirect_pos != -1) { // Xử lý redirect input/output
        int fd_in = -1, fd_out = -1;
        if (input_redirect_pos != -1) {
            fd_in = open(args[input_redirect_pos + 1], O_RDONLY);
            if (fd_in < 0) { perror("Failed to open input file"); exit(1); }
            args[input_redirect_pos] = NULL;
        }
        if (output_redirect_pos != -1) {
            fd_out = open(args[output_redirect_pos + 1], O_WRONLY | O_CREAT);
            if (fd_out < 0) { perror("Failed to open output file"); exit(1); }
            args[output_redirect_pos] = NULL;
        }

        if (fork() == 0) {
            if (fd_in != -1) {
                dup2(fd_in, STDIN_FILENO);
                close(fd_in);
            }
            if (fd_out != -1) {
                dup2(fd_out, STDOUT_FILENO);
                close(fd_out);
            }
            execvp(args[0], args);
            perror("execvp failed"); exit(1);
        }
        if (fd_in != -1) close(fd_in);
        if (fd_out != -1) close(fd_out);
        wait(NULL);
    } else { // Chạy lệnh bình thường
        execvp(args[0], args);
        perror("execvp failed"); exit(1);
    }
}

int main() {
    char buffer[MAX_LINE];
    char* args[MAX_COMMANDS];
    char last_command[MAX_LINE] = "";

    while (1) {
        printf("osh>> "); fflush(stdout);
        if (!fgets(buffer, MAX_LINE, stdin)) break;

        int background = 0;

        if (strcmp(buffer, "!!\n") == 0) {
            if (strlen(last_command) == 0) {
                printf("No commands in history!\n"); continue;
            }
            strcpy(buffer, last_command);
            printf("%s", buffer);
        } else {
            strcpy(last_command, buffer);
        }

        tokenize(buffer, args, &background);
        if (!args[0]) continue;

        pid_t pid = fork();
        if (pid == 0) { // Tiến trình con
            execute_command(args);
        } else { // Tiến trình cha
            if (!background) {
                wait(NULL);
            }
        }
    }
    return 0;
}

