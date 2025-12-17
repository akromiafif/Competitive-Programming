
import random

def generate_simple():
    # As requested by user
    cases = []
    # 1. Example from problem
    cases.append("5\n1 2 3 4 6")
    # 2. Impossible
    cases.append("3\n1 5 6")
    # 3. Simple equal
    cases.append("3\n1 2 3")
    return cases

def generate_complex():
    cases = []
    # 1. Medium size, random
    # Generate 10 cases
    for i in range(10):
        N = 50 # Max standard for this kind of recursion/DP usually
        # To stress test slightly, let's vary N and range
        
        # Mix of "likely to solve" and "unlikely"
        # Case 1-5: Small values, larger N (High probability of solution)
        if i < 5:
            arr = [random.randint(1, 20) for _ in range(N)]
            cases.append(f"{N}\n{' '.join(map(str, arr))}")
        # Case 6-8: Large values (sparse sum)
        elif i < 8:
            arr = [random.randint(100, 1000) for _ in range(N)]
            cases.append(f"{N}\n{' '.join(map(str, arr))}")
        # Case 9-10: N=20 but random (pure recursion might fail without DP)
        else:
            n_small = 25
            arr = [random.randint(1, 50) for _ in range(n_small)]
            cases.append(f"{n_small}\n{' '.join(map(str, arr))}")
            
    return cases

simple = generate_simple()
complex_cases = generate_complex()

with open("tests_simple.txt", "w") as f:
    f.write(f"{len(simple)}\n")
    for c in simple:
        f.write(c + "\n")

with open("tests_complex.txt", "w") as f:
    f.write(f"{len(complex_cases)}\n")
    for c in complex_cases:
        f.write(c + "\n")
