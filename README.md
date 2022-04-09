1. Uruchomienie w folderze build/lib:

java -jar botrouteplanner.jar grid-1.txt job-1.txt

java -jar botrouteplanner.jar grid-2.txt job-2.txt





2. Dodatkowe zadanie otwarte:
I sposób:
1. Dijkstra od początkowej pozycji bota.
2. Wybrać minimum z liczby modułów zawierających szukany target i z liczby stacji odbiorczych
-  Jeśli minimum to stacje odbiorcze to dla każdego z nich odpalamy dijkstrę
-  wpp. dla każdego modułów z targetem puszczamy dijkstre

3. Iterujemy po modułuach zawierjących target,
    dla każdego z nich iterujemy po stacjach odbiorczych i szukamy najmniejszej ścieżki

O(ElogV * min(liczba modułów z targetem, liczba stacji odbiorczyh))

minusy gdy liczba modułów z targetem jest duża lepiej rozważyć użycie algorymtu Floyda Warshalla

II sposób:
1. Puścić algorytm Floyda Warshalla (znajduje najkrótsze ścieżki między każdą parą wierzchołków

2. Iterujemy po modułach zawierających target i szukamy najkrótszej ścieżki,

a najkrótsza ścieżka to: start ---> target + pickup ---> receive

O(V^3)


3. Sposób rozwiązania
    1. Dijkstra od pozycji startowej O(ElogV)
    2. Dijkstra od pozycji końcowej O(ElogV)
    3. Iteracja po modułach zawierających szukany produkt i wyznaczenie najkrótszej ścieżki,

    O(ElogV)

gdzie

E - liczba krawędzi

V - liczba wierzchołków

