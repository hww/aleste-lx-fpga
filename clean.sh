#!/bin/bash

# === CLEAN PROJECT SCRIPT ===
# Removes all files and directories listed in .gitignore

# Color definitions
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== PROJECT CLEANUP SCRIPT ===${NC}"

# Check if .gitignore exists
if [ ! -f .gitignore ]; then
    echo -e "${RED}Error: .gitignore file not found${NC}"
    exit 1
fi

# Read .gitignore and process each line
while IFS= read -r line || [[ -n "$line" ]]; do
    # Skip empty lines and comments
    if [[ -z "$line" ]] || [[ "$line" =~ ^\s*# ]]; then
        continue
    fi
    
    # Remove leading slash if present
    pattern="${line#/}"
    
    # Handle directory patterns (ending with /)
    if [[ "$pattern" == */ ]]; then
        pattern="${pattern%/}"
        echo -e "${YELLOW}Removing directory: ${pattern}${NC}"
        find . -type d -name "$pattern" -exec rm -rf {} + 2>/dev/null
    else
        echo -e "${YELLOW}Removing files: ${pattern}${NC}"
        find . -type f -name "$pattern" -delete 2>/dev/null
    fi
done < .gitignore

echo -e "${GREEN}Cleanup completed successfully${NC}"
