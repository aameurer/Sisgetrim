import pandas as pd
import sys

file_path = r"c:\sisgetrim\CAD.FISCAL (ITBI) FERNANDO 2025-01-19 importar no sisgetrim.xlsx"

try:
    xl = pd.ExcelFile(file_path)
    print(f"Abas encontradas: {xl.sheet_names}")
    
    for sheet_name in xl.sheet_names:
        print(f"\n--- Aba: {sheet_name} ---")
        df = pd.read_excel(file_path, sheet_name=sheet_name, nrows=10, header=None)
        print(df.to_string())
except Exception as e:
    print(f"Erro ao ler o arquivo: {e}")
