#!/usr/bin/env bash
# =====================================================================
# Rebrand LivrApp -> ASAP (code + config)
# À lancer depuis la RACINE du dépôt (là où se trouve .github/, apps/).
# Idempotent sur les contenus ; déplace les packages Java com.livrapp -> com.asap.
# =====================================================================
set -euo pipefail

# 0) Garde-fou : on est bien à la racine du dépôt ?
if [ ! -f "apps/api/pom.xml" ]; then
  echo "ERREUR : lance ce script depuis la racine du dépôt (apps/api/pom.xml introuvable)." >&2
  exit 1
fi

echo "1/3  Déplacement des packages Java com/livrapp -> com/asap ..."
for base in "apps/api/src/main/java/com" "apps/api/src/test/java/com"; do
  if [ -d "$base/livrapp" ]; then
    git mv "$base/livrapp" "$base/asap"
    echo "     déplacé : $base/livrapp -> $base/asap"
  fi
done

echo "2/3  Remplacement des références dans les fichiers versionnés ..."
# On ne touche qu'aux fichiers texte SUIVIS par git (jamais .git, .idea, target, binaires).
git ls-files -z -- \
    '*.java' '*.xml' '*.yml' '*.yaml' '*.md' '*.sql' '*.properties' '*.gitignore' 'Dockerfile*' \
  | xargs -0 sed -i \
      -e 's/com\.livrapp/com.asap/g' \
      -e 's/LivrApp/ASAP/g' \
      -e 's/livrapp/asap/g'

echo "3/3  Vérification ..."
rest=$(git grep -I -l -e 'com\.livrapp' -e 'LivrApp' -e 'livrapp' -- \
        '*.java' '*.xml' '*.yml' '*.yaml' '*.md' '*.sql' '*.properties' 2>/dev/null | wc -l || true)
echo "     Fichiers contenant encore une occurrence : $rest"
echo ""
echo "Terminé. Vérifie avec :  git status   puis   git diff --stat"
