/*
 * Filename: DiningPhilosophers.c
 * Author: Vũ Tùng Lâm
 * Modified by: Nguyễn Chí Công
 * Description: Implementation of the Dining Philosophers problem using pthreads, mutexes, and semaphores.
 *
 * This program models the classic Dining Philosophers problem, ensuring that multiple philosophers 
 * can eat without leading to deadlock or starvation. It uses:
 * - Mutexes to control access to shared resources (chopsticks/forks).
 * - A semaphore to limit the number of philosophers trying to eat simultaneously.
 * - A readers-writers model to manage access to the shared 'duaCuaAi' array.
 *
 * Compilation:
 * gcc -o dining dining.c -lpthread -lrt
 *
 * Usage:
 * ./dining
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>

#define N 5 // Number of philosophers

pthread_mutex_t modifyReadsDuaCuaAi;
pthread_mutex_t writeDuaCuaAi;
int volatile readsDuaCuaAi;

sem_t dining;
pthread_mutex_t dua[N];
pthread_t thread[N];
int duaCuaAi[N];

// Initialize Readers-Writers model
void initializeReadersWritersModel() {
	if (
		pthread_mutex_init(&modifyReadsDuaCuaAi, NULL) != 0 ||
		pthread_mutex_init(&writeDuaCuaAi, NULL) != 0
	) {
		fprintf(stderr, "ERROR: Could not initialize mutex/semaphore for Readers/Writers model\n");
		exit(EXIT_FAILURE);
	}
}

void beforeWriting() {
	pthread_mutex_lock(&writeDuaCuaAi);
}
void afterWriting() {
	pthread_mutex_unlock(&writeDuaCuaAi);
}
void beforeReading() {
	pthread_mutex_lock(&modifyReadsDuaCuaAi);
	++readsDuaCuaAi;
	if (readsDuaCuaAi == 1) pthread_mutex_lock(&writeDuaCuaAi);
	pthread_mutex_unlock(&modifyReadsDuaCuaAi);
}
void afterReading() {
	pthread_mutex_lock(&modifyReadsDuaCuaAi);
	--readsDuaCuaAi;
	if (readsDuaCuaAi == 0) pthread_mutex_unlock(&writeDuaCuaAi);
	pthread_mutex_unlock(&modifyReadsDuaCuaAi);
}

// Philosopher function
void* philosopher(void* arg) {
	int i = (int)(unsigned long long)arg;

	while (1) {
		// Pick up left chopstick
		pthread_mutex_lock(&dua[i]);
		beforeWriting();
		duaCuaAi[i] = i;
		afterWriting();

		// Pick up right chopstick
		pthread_mutex_lock(&dua[(i + 1) % N]);
		beforeWriting();
		duaCuaAi[(i + 1) % N] = i;
		afterWriting();

		usleep(5); // Simulate eating

		// Put down left chopstick
		beforeWriting();
		duaCuaAi[i] = -1;
		afterWriting();
		pthread_mutex_unlock(&dua[i]);

		// Put down right chopstick
		beforeWriting();
		duaCuaAi[(i + 1) % N] = -1;
		afterWriting();
		pthread_mutex_unlock(&dua[(i + 1) % N]);
	}
}

int main() {
	if (sem_init(&dining, 0, N - 1) != 0) {
		fprintf(stderr, "ERROR: Could not initialize semaphore `dining`\n");
		exit(EXIT_FAILURE);
	}

	if (pthread_mutex_init(&writeDuaCuaAi, NULL) != 0) {
		fprintf(stderr, "ERROR: Could not initialize mutex `writeDuaCuaAi`\n");
		exit(EXIT_FAILURE);
	}

	for (int i = 0; i < N; ++i) {
		if (pthread_mutex_init(&dua[i], NULL) != 0) {
			fprintf(stderr, "ERROR: Could not initialize mutex `dua[%d]`\n", i);
			exit(EXIT_FAILURE);
		}
		duaCuaAi[i] = -1;
	}

	for (int i = 0; i < N; ++i) {
		if (pthread_create(&thread[i], NULL, &philosopher, (void*)((unsigned long long)i)) != 0) {
			fprintf(stderr, "ERROR: Could not create thread `%d`\n", i);
			exit(EXIT_FAILURE);
		}
	}

	initializeReadersWritersModel();

	do {
		usleep(5);
		beforeReading();
		for (int i = 0; i < N; ++i) {
			if (duaCuaAi[i] == -1) {
				printf("Dua %d khong ai cam\n", i);
			} else {
				printf("Dua %d cua nha triet hoc %d\n", i, duaCuaAi[i]);
			}
		}
		afterReading();
		printf("=============\n");
	} while (1);

	// Cleanup
	for (int i = 0; i < N; ++i) {
		pthread_mutex_destroy(&dua[i]);
	}
	sem_destroy(&dining);

	return 0;
}
