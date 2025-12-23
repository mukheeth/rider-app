# Script to run database migrations
# This will create all necessary tables for user registration

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Running Database Migrations" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$env:PGPASSWORD = "root"
$DB_HOST = "localhost"
$DB_PORT = "5433"
$DB_NAME = "ridehailing"
$DB_USER = "postgres"

$migrationDir = "database\migrations"

# Check if migration directory exists
if (-not (Test-Path $migrationDir)) {
    Write-Host "[ERROR] Migration directory not found: $migrationDir" -ForegroundColor Red
    exit 1
}

Write-Host "Running migrations in order..." -ForegroundColor Yellow
Write-Host ""

# Run migrations in order
$migrations = @(
    "001_create_roles_table.sql",
    "002_create_users_table.sql",
    "003_create_riders_table.sql"
)

foreach ($migration in $migrations) {
    $migrationPath = Join-Path $migrationDir $migration
    
    if (-not (Test-Path $migrationPath)) {
        Write-Host "[SKIP] Migration file not found: $migration" -ForegroundColor Yellow
        continue
    }
    
    Write-Host "Running: $migration..." -ForegroundColor Cyan
    $result = psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f $migrationPath 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  [OK] $migration completed" -ForegroundColor Green
    } else {
        Write-Host "  [FAIL] $migration failed" -ForegroundColor Red
        Write-Host "  Error: $result" -ForegroundColor Red
        $env:PGPASSWORD = $null
        exit 1
    }
}

Write-Host ""
Write-Host "Verifying tables..." -ForegroundColor Yellow
$tables = psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -t -c "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name;" 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "[OK] Tables created:" -ForegroundColor Green
    $tables | ForEach-Object { 
        $table = $_.Trim()
        if ($table -ne "") {
            Write-Host "  - $table" -ForegroundColor Gray
        }
    }
    
    Write-Host ""
    Write-Host "Checking roles..." -ForegroundColor Yellow
    $roles = psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -t -c "SELECT role_name FROM roles ORDER BY role_name;" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] Roles:" -ForegroundColor Green
        $roles | ForEach-Object { 
            $role = $_.Trim()
            if ($role -ne "") {
                Write-Host "  - $role" -ForegroundColor Gray
            }
        }
    }
} else {
    Write-Host "[FAIL] Could not verify tables" -ForegroundColor Red
}

$env:PGPASSWORD = $null

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Migration Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "You can now register users and they will be saved to the database." -ForegroundColor Green

