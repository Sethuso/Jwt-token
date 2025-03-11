git init                            # Initialize a new Git repository
git add .                           # Stage all files for commit
git commit -m "Initial commit"      # Make the first commit
git remote add origin https://github.com/your-username/your-repo.git   # Link local repo to GitHub
git branch -M main                 # Rename branch to 'main' (if it's not already 'main')
git push -u origin main            # Push 'main' branch to GitHub
git checkout -b feature-branch     # Create and switch to a new branch
git push -u origin feature-branch  # Push the new branch to GitHub
