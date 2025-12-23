# Script to set PostgreSQL password to "root"
# This script will prompt you for the current password, then set it to "root"

Write-Host "Setting PostgreSQL password to 'root'..." -ForegroundColor Cyan
Write-Host ""
Write-Host "You will be prompted for the CURRENT PostgreSQL password." -ForegroundColor Yellow
Write-Host "If you don't know it, you may need to:" -ForegroundColor Yellow
Write-Host "  1. Check pgAdmin or other PostgreSQL tools" -ForegroundColor Yellow
Write-Host "  2. Or reset it using PostgreSQL admin methods" -ForegroundColor Yellow
Write-Host ""

# Try to set the password
# Note: This will prompt for current password
psql -U postgres -d postgres -c "ALTER USER postgres PASSWORD 'root';"

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "[SUCCESS] Password has been set to 'root'" -ForegroundColor Green
    Write-Host ""
    Write-Host "Testing connection..." -ForegroundColor Cyan
    
    $env:PGPASSWORD = "root"
    $testResult = psql -h localhost -p 5432 -U postgres -d postgres -c "SELECT current_user;" 2>&1
    $env:PGPASSWORD = $null
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[SUCCESS] Connection test passed!" -ForegroundColor Green
        Write-Host ""
        Write-Host "Your database is now configured correctly." -ForegroundColor Green
        Write-Host "You can now run: .\test_db_connection.ps1" -ForegroundColor Cyan
    } else {
        Write-Host "[WARNING] Password was set but connection test failed." -ForegroundColor Yellow
        Write-Host "Error: $testResult" -ForegroundColor Red
    }
} else {
    Write-Host ""
    Write-Host "[FAILED] Could not set password." -ForegroundColor Red
    Write-Host "Possible reasons:" -ForegroundColor Yellow
    Write-Host "  1. Current password is incorrect" -ForegroundColor Yellow
    Write-Host "  2. User doesn't have permission to change password" -ForegroundColor Yellow
    Write-Host "  3. PostgreSQL authentication is configured differently" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Try running manually:" -ForegroundColor Cyan
    Write-Host "  psql -U postgres -d postgres" -ForegroundColor White
    Write-Host "  Then run: ALTER USER postgres PASSWORD 'root';" -ForegroundColor White
}

